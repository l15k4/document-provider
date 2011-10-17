package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import cz.instance.utils.docs.DocumentGenerator;

public class DOCxGenerator extends DocumentGenerator {

	@Override
	protected void populate(File file, String content) throws Exception {
		createDOCxDocument(content, file);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createDOCxDocument(sample, sampleFile);
	}

	private void createDOCxDocument(String from, File file) throws Exception {

    	        XWPFDocument doc = new XWPFDocument();

		        XWPFParagraph p1 = doc.createParagraph();
		        p1.setAlignment(ParagraphAlignment.CENTER);
		        p1.setBorderBottom(Borders.DOUBLE);
		        p1.setBorderTop(Borders.DOUBLE);

		        p1.setBorderRight(Borders.DOUBLE);
		        p1.setBorderLeft(Borders.DOUBLE);
		        p1.setBorderBetween(Borders.SINGLE);

		        p1.setVerticalAlignment(TextAlignment.TOP);

		        XWPFRun r1 = p1.createRun();
		        r1.setBold(true);
		        r1.setText("The quick brown fox");
		        r1.setBold(true);
		        r1.setFontFamily("Courier");
		        r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
		        r1.setTextPosition(100);

		        XWPFParagraph p2 = doc.createParagraph();
		        p2.setAlignment(ParagraphAlignment.RIGHT);

		        //BORDERS
		        p2.setBorderBottom(Borders.DOUBLE);
		        p2.setBorderTop(Borders.DOUBLE);
		        p2.setBorderRight(Borders.DOUBLE);
		        p2.setBorderLeft(Borders.DOUBLE);
		        p2.setBorderBetween(Borders.SINGLE);

		        XWPFRun r2 = p2.createRun();
		        r2.setText("jumped over the lazy dog");
		        r2.setStrike(true);
		        r2.setFontSize(20);

		        XWPFRun r3 = p2.createRun();
		        r3.setText("and went away");
		        r3.setStrike(true);
		        r3.setFontSize(20);
		        r3.setSubscript(VerticalAlign.SUPERSCRIPT);


		        XWPFParagraph p3 = doc.createParagraph();
		        p3.setWordWrap(true);
		        p3.setPageBreak(true);
		                
		        //p3.setAlignment(ParagraphAlignment.DISTRIBUTE);
		        p3.setAlignment(ParagraphAlignment.BOTH);
		        p3.setSpacingLineRule(LineSpacingRule.EXACT);

		        p3.setIndentationFirstLine(600);

		        XWPFRun r4 = p3.createRun();
		        r4.setTextPosition(20);
		        r4.setText(from);

		        FileOutputStream out = new FileOutputStream(file);
		        doc.write(out);
		        out.close();
	}
}
