package cz.instance.utils.docs;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.instance.utils.docs.impl.DOCGenerator;
import cz.instance.utils.docs.impl.DOCxGenerator;
import cz.instance.utils.docs.impl.HTMLGenerator;
import cz.instance.utils.docs.impl.ODFGenerator;
import cz.instance.utils.docs.impl.PDFGenerator;
import cz.instance.utils.docs.impl.PPTGenerator;
import cz.instance.utils.docs.impl.TXTGenerator;
import cz.instance.utils.docs.impl.XLSGenerator;
import cz.instance.utils.docs.impl.XLSxGenerator;

public class DocumentProvider {

	private static Logger logger = LoggerFactory.getLogger(DocumentProvider.class);
	private static final Map<String, List<Document>> documents = new HashMap<String, List<Document>>();
	
	public static String htmlType = "html";
	public static String txtType = "txt";
	public static String pdfType = "pdf";
	public static String odtType = "odt";
	public static String docxType = "docx";
	public static String docType = "doc";
	public static String xlsxType = "xlsx";
	public static String xlsType = "xls";
	public static String pptType = "ppt";

	public static Map<String, List<Document>> getDocuments() throws Exception {
		
		if(!documents.isEmpty())
			return documents;
		
		File docs = new File(DocumentProvider.class.getClassLoader().getResource("docs").toURI());
		if (!docs.exists())
			throw new Exception("docs directory doesn't exist");

		InputStream fis = DocumentGenerator.class.getClass().getResourceAsStream("/fonts/FreeSans.ttf");

		List<Document> textContentEntities = DocumentGenerator.exe(htmlType, docs);
		List<Document> textEntities = new TXTGenerator().createDocs(txtType, docs, textContentEntities);
		List<Document> htmlEntities = new HTMLGenerator().createDocs(htmlType, docs, textEntities);
		List<Document> pdfEntities = new PDFGenerator(fis).createDocs(pdfType, docs, textEntities);
		List<Document> odtEntities = new ODFGenerator().createDocs(odtType, docs, textEntities);
		List<Document> docxEntities = new DOCxGenerator().createDocs(docxType, docs, textEntities);
		List<Document> docEntities = new DOCGenerator().createDocs(docType, docs, textEntities);
		List<Document> xlsxEntities = new XLSxGenerator().createDocs(xlsxType, docs, textEntities);
		List<Document> xlsEntities = new XLSGenerator().createDocs(xlsType, docs, textEntities);
		List<Document> pptEntities = new PPTGenerator().createDocs(pptType, docs, textEntities);

		documents.put(txtType, textEntities);
		documents.put(htmlType, htmlEntities);
		documents.put(pdfType, pdfEntities);
		documents.put(odtType, odtEntities);
		documents.put(docxType, docxEntities);
		documents.put(docType, docEntities);
		documents.put(xlsxType, xlsxEntities);
		documents.put(xlsType, xlsEntities);
		documents.put(pptType, pptEntities);

		return documents;
	}
	
	
	public static Map<String, List<Document>> getDocsWithKeyPrefix(String prefix) {
		prefix = prefix + ".";
		Map<String, List<Document>> newMap = new HashMap<String, List<Document>>();
		
		for(String ext : documents.keySet()){
			newMap.put(prefix + ext, documents.get(ext));
		}
		return newMap;
	}

	public static Document[] getLangsByType(String type, String...langCodes) throws Exception {
		List<Document> all = getAllLangsByType(type);
		List<Document> result = new ArrayList<Document>(langCodes.length);
		for(Document e : all) {
			if(ArrayUtils.contains(langCodes, e.getState()))
					result.add(e);
		}
		Document[] array = new Document[result.size()];
		return result.toArray(array);
	}
	
	public static List<Document> getAllLangsByType(String type) throws Exception {
		Map<String, List<Document>> entities = getDocuments();
		return entities.get(type);
	}

	public static Document[] getAllLangsByTypeAsArray(String type) throws Exception {
		List<Document> list = getAllLangsByType(type);
		Document[] array = new Document[list.size()];
		return list.toArray(array);
	}

	public static List<Document> getAllTypesByLang(String lang) throws Exception {
		Map<String, List<Document>> entities = getDocuments();
		List<Document> result = new ArrayList<Document>();
		for (Map.Entry<String, List<Document>> entry : entities.entrySet()) {
			for (Document entity : entry.getValue()) {
				if (entity.getState().equals(lang)) {
					result.add(entity);
					break;
				}
			}
		}
		return result;
	}

	public static Map<String, Document> getAllTypesByLangAsMap(String lang) throws Exception {
		Map<String, List<Document>> entities = getDocuments();
		Map<String, Document> result = new HashMap<String, Document>();
		for (Map.Entry<String, List<Document>> entry : entities.entrySet()) {
			for (Document entity : entry.getValue()) {
				if (entity.getState().equals(lang)) {
					result.put(entity.getType(), entity);
					break;
				}
			}
		}
		return result;
	}

	public static Document getDocByTypeAndLang(String type, String state) throws Exception {
		Map<String, List<Document>> entities = getDocuments();
		Document result = null;
		for (Map.Entry<String, List<Document>> entry : entities.entrySet()) {
			if (!entry.getKey().equals(type))
				continue;
			for (Document entity : entry.getValue()) {
				if (entity.getState().equals(state)) {
					result = entity;
					break;
				}
			}
		}
		return result;
	}

	public static List<Document> docsAsList() throws Exception {
		Map<String, List<Document>> entities = getDocuments();
		List<Document> result = new ArrayList<Document>();
		for (Map.Entry<String, List<Document>> entry : entities.entrySet()) {
			for (Document entity : entry.getValue()) {
				result.add(entity);
			}
		}
		return result;
	}

	public static List<File> getSampleFilesFrom(List<Document> entities) {
		List<File> result = new LinkedList<File>();
		for (Document entity : entities) {
			result.add(entity.getSampleFile());
		}
		return result;
	}

	public static List<File> getFilesFrom(List<Document> entities) {
		List<File> result = new LinkedList<File>();
		for (Document entity : entities) {
			result.add(entity.getFile());
		}
		return result;
	}

	private static void logContent(Map<String, List<Document>> entities) {
		for (Map.Entry<String, List<Document>> entry : entities.entrySet()) {
			System.out.println("-------------");
			System.out.println(entry.getKey());
			for (Document entity : entry.getValue()) {
				System.out.println("state : " + entity.getState() + "\n");
				System.out.println("type : " + entity.getType() + "\n");
				System.out.println("sample : " + entity.getSample() + "\n");
				System.out.println("-------------");
			}
		}
	}

	private static void logContent(List<Document> entities) {

		for (Document entity : entities) {
			System.out.println("state : " + entity.getState() + "\n");
			System.out.println("type : " + entity.getType() + "\n");
			System.out.println("sample : " + entity.getSample() + "\n");
			System.out.println("-------------");
		}
	}
}
