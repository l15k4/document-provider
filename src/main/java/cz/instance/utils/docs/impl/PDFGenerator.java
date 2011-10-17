package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfWriter;

import cz.instance.utils.docs.DocumentGenerator;


public class PDFGenerator extends DocumentGenerator {

	public byte[] fontByteArray;
	public InputStream fis;

	public PDFGenerator(InputStream fis) throws IOException {
		fontByteArray = IOUtils.toByteArray(fis);
		this.fis = fis;
	}

	@Override
	protected void populate(File file, String content) throws Exception {
		createITextDocument(content, file);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createITextDocument(sample, sampleFile);	
	}

	private void createITextDocument(String from, File to) throws Exception {

		Document document = new Document(new RectangleReadOnly(630, 850));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(to));
		document.open();
		writer.getAcroForm().setNeedAppearances(true);
		BaseFont unicode = BaseFont.createFont("FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontByteArray, null);

		FontSelector fs = new FontSelector();
		fs.addFont(new Font(unicode, 5));

		addContent(document, getParagraphs(from), fs);
		document.close();
	}
	
	private void createPdfBoxDocument(File from, File to) throws IOException, FileNotFoundException, COSVisitorException {

		PDDocument document = null;
		try {
			document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			PDFont font = PDTrueTypeFont.loadTTF(document, fis);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			contentStream.beginText();
			contentStream.setFont(font, 5);
			contentStream.drawString(FileUtils.readFileToString(from));
			contentStream.endText();
			contentStream.close();
			document.save(new FileOutputStream(to));
		} finally {
			if (document != null)
				document.close();
		}
	}
	
	private void addContent(Document document, List<String> paragraphs, FontSelector fs) throws DocumentException {

		for (int i = 0; i < paragraphs.size(); i++) {
			Phrase phrase = fs.process(paragraphs.get(i));
			document.add(new Paragraph(phrase));
		}
	}
}
