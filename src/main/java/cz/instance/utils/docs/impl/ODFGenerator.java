package cz.instance.utils.docs.impl;

import java.io.File;

import org.odftoolkit.odfdom.doc.OdfTextDocument;

import cz.instance.utils.docs.DocumentGenerator;

public class ODFGenerator extends DocumentGenerator {

	@Override
	protected void populate(File file, String content) throws Exception {
		createODTfile(content, file);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createODTfile(sample, sampleFile);
	}

	private void createODTfile(String from, File to) throws Exception {
		OdfTextDocument odt = OdfTextDocument.newTextDocument();
		odt.addText(from);
		odt.save(to);
	}
}
