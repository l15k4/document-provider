package cz.instance.utils.docs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tika.mime.MediaType;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

public abstract class DocumentGenerator {

	private static Logger logger = LoggerFactory.getLogger(DocumentGenerator.class);

	public static enum ST {
		BG, ES, CS, DA, DE, ET, EL, EN, FR, IT, LV, LT, HU, MT, NL, PL, PT, RO, SK, SL, FI, SV
	};

	public static enum STATES {
		bg, es, cs, da, de, et, el, en, fr, it, lv, lt, hu, mt, nl, pl, pt, ro, sk, sl, fi, sv
	};

	public static String URL = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=OJ:C:2008:224:0130:01:";
	public static String HtmlPref = ":HTML";
	public static String PdfPref = ":PDF";
	public static int interval = 3000;
	public static int sampleLine = 23;
	public static int sampleLenght = 0;
	public static int minWordLength = 3;

	public static String htmlType = "html";
	public static String txtType = "txt";
	public static String pdfType = "pdf";
	public static String odtType = "odt";
	public static String docxType = "docx";
	public static String docType = "doc";
	public static String xlsxType = "xlsx";
	public static String xlsType = "xls";
	public static String pptType = "ppt";

	public static MediaType odtMT = MediaType.application("vnd.oasis.opendocument.text");
	public static MediaType pdfMT = MediaType.application("pdf");
	public static MediaType docxMT = MediaType.application("vnd.openxmlformats-officedocument.wordprocessingml.document");
	public static MediaType docMT = MediaType.application("msword");
	public static MediaType xlsMT = MediaType.application("vnd.ms-excel");
	public static MediaType xlsxMT = MediaType.application("vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	public static MediaType pptMT = MediaType.application("vnd.ms-powerpoint");
	public static MediaType htmlMT = MediaType.text("html");
	public static MediaType txtMT = MediaType.TEXT_PLAIN;

	public static List<Document> exe(String type, File parent) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		List<Document> entities = getNewEntities(URL);
		File docTypeDir = createChildDir(parent, "origHtml");

		for (Document entry : entities) {
			File file = new File(docTypeDir, entry.getState() + "." + type);
			if (file.exists()) {
				entry.setContent(getTextFromHtml(file));
				continue;
			}
			logger.info("getting HTML");
			HttpGet get = new HttpGet(entry.getUrl() + entry.getState().toUpperCase() + HtmlPref);
			HttpResponse response;
			try {
				response = httpclient.execute(get);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			HttpEntity entity = response.getEntity();
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				continue;
			}
			entity.writeTo(stream);
			stream.close();

			entry.setContent(getTextFromHtml(file));
			Thread.sleep(interval);
		}

		return entities;
	}

	public List<Document> createDocs(String type, File parent, List<Document> entities) throws Exception {

		List<Document> newEntities = getNewEntities(URL);

		File docTypeDir = createChildDir(parent, type);
		File sampleDir = createChildDir(docTypeDir, "sample");

		for (int i = 0; i < entities.size(); i++) {

			File file = new File(docTypeDir, entities.get(i).getState() + "." + type);
			File sampleFile = new File(sampleDir, entities.get(i).getState() + "." + type);
			if (file.exists()) {
				String content = entities.get(i).getContent();
				List<String> words = getWords(content);
				String sample = getSample(content, sampleLine);
				List<String> sampleWords = getWords(sample);
				createSampleFile(sampleFile, sample);
				long checksum = FileUtils.checksumCRC32(file);
				setUpEntity(type, newEntities.get(i), file, file.length(), checksum, content, words, sample, sampleWords, sampleFile, getMetadata(type));
				continue;
			}
			String content = entities.get(i).getContent();
			List<String> words = getWords(content);
			String sample = getSample(content, sampleLine);
			List<String> sampleWords = getWords(sample);

			populate(file, content);
			populateSample(sampleFile, sample);
			long checksum = FileUtils.checksumCRC32(file);
			setUpEntity(type, newEntities.get(i), file, file.length(), checksum, content, words, sample, sampleWords, sampleFile, getMetadata(type));
		}
		return newEntities;
	}

	protected abstract void populate(File file, String content) throws Exception;

	protected abstract void populateSample(File sampleFile, String sample) throws Exception;

	private static String getTextFromHtml(File file) throws Exception {

		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(file);

		TagNode[] nodes = node.getElementsByName("TXT_TE", true);
		if (nodes.length != 1)
			throw new Exception("TXT_TE nodes size : " + nodes.length);
		TagNode tg = nodes[0];
		TagNode[] paragraphs = tg.getElementsByName("p", false);

		StringBuffer output = new StringBuffer();
		String cr = System.getProperty("line.separator");

		for (TagNode p : paragraphs) {
			StringBuffer text = p.getText();
			output.append(text);
			output.append(cr);
		}
		return output.toString();
	}

	private static File createChildDir(File parent, String type) throws Exception {
		File docTypeDir = new File(parent, type);
		boolean exists = docTypeDir.exists() ? true : docTypeDir.mkdir();
		if (!exists || !docTypeDir.isDirectory())
			throw new Exception("docTypeDir " + type + " doesn't exist or is not a directory ?");
		return docTypeDir;
	}

	private static List<Document> getNewEntities(String URL) {
		List<Document> entities = new ArrayList<Document>();
		for (STATES state : STATES.values()) {
			entities.add(new Document(System.currentTimeMillis(), URL, state.toString()));
		}
		return entities;
	}

	protected void createSampleFile(File sampleFile, String sample) throws IOException, Error {
		if (!sampleFile.exists()) {
			if (sampleFile.createNewFile()) {
				BufferedWriter out = new BufferedWriter(new FileWriter(sampleFile));
				out.write(sample);
				out.close();
			} else
				throw new Error("Sample file creation fail");
		}
	}

	private void setUpEntity(String type, Document e, File file, long size, long checksum, String content, List<String> words, String sample, List<String> sampleWords, File sampleFile, MediaType mt) {
		e.setWordCount(getWordCount(content));
		e.setSampleWordCount(getWordCount(sample));
		e.setContent(content);
		e.setContent(content);
		e.setType(type);
		e.setFile(file);
		e.setSize(size);
		e.setChecksum(checksum);
		e.setSample(sample);
		e.setSample(sample);
		e.setSampleFile(sampleFile);
		e.setMediaType(mt);
		e.setWords(words);
		e.setSampleWords(sampleWords);
		e.setMediaType(mt);
	}

	private int getWordCount(String text) {
		Iterable<String> wordsInFile = Splitter.on(' ').trimResults().omitEmptyStrings().split(text);
		int i = 0;
		for (String word : wordsInFile) {
			if (word.length() >= minWordLength) {
				i++;
			}
		}
		return i;
	}

	private List<String> getWords(String text) {
		Iterable<String> wordsInFile = Splitter.on(' ').trimResults().omitEmptyStrings().split(text);
		List<String> list = new ArrayList<String>();
		int i = 0;
		for (String word : wordsInFile) {
			if (word.length() >= minWordLength) {
				list.add(word);
			}
		}
		return list;
	}

	private MediaType getMetadata(String type) {
		if (type.equals(txtType))
			return txtMT;
		else if (type.equals(htmlType))
			return htmlMT;
		else if (type.equals(pdfType))
			return pdfMT;
		else if (type.equals(odtType))
			return odtMT;
		else if (type.equals(docxType))
			return docxMT;
		else if (type.equals(docType))
			return docMT;
		else if (type.equals(pptType))
			return pptMT;
		else if (type.equals(xlsxType))
			return xlsxMT;
		else if (type.equals(xlsType))
			return xlsMT;
		else
			throw new Error();
	}

	private String getSample(String text, int line) throws Exception {
		String cr = "\n";
		int currentCharIndex = 0;
		int crIndex = 0;

		for (int i = 0; i <= line; i++) {
			currentCharIndex = text.indexOf(cr, currentCharIndex) + 3;
		}
		crIndex = text.indexOf(cr, currentCharIndex);
		if (crIndex == 0)
			throw new Exception("CR fail");
		
		int resultingLength = crIndex - currentCharIndex;
		
		crIndex -= (sampleLenght != 0 && resultingLength > sampleLenght) ? resultingLength - sampleLenght : 0;
		
		return text.substring(currentCharIndex, crIndex);
	}

	protected List<String> getParagraphs(String content) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		LineNumberReader reader = new LineNumberReader(new StringReader(content));

		String s;
		while ((s = reader.readLine()) != null) {
			list.add(s);
		}
		return list;
	}
}