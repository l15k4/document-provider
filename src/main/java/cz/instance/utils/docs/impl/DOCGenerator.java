package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterProperties;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cz.instance.utils.docs.DocumentGenerator;

public class DOCGenerator extends DocumentGenerator {

	@Override
	protected void populate(File file, String content) throws Exception {
		createDOCDocument(content, file);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createDOCDocument(sample, sampleFile);
	}

	private void createDOCDocument(String from, File file) throws Exception {
	
		// POI apparently can't create a document from scratch,
		// so we need an existing empty dummy document
		POIFSFileSystem fs = new POIFSFileSystem(DOCGenerator.class.getClass().getResourceAsStream("/poi/template.doc"));
		HWPFDocument doc = new HWPFDocument(fs);
	
		Range range = doc.getRange();
		Paragraph par1 = range.getParagraph(0);
	
		CharacterRun run1 = par1.insertBefore(from, new CharacterProperties());
		run1.setFontSize(11);
		doc.write(new FileOutputStream(file));

	}
}