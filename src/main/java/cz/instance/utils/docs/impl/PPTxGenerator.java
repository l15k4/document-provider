package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;

import cz.instance.utils.docs.DocumentGenerator;

public class PPTxGenerator extends DocumentGenerator {

	@Override
	protected void populate(File file, String content) throws Exception {
		createPPTxDocument(content, file);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createPPTxDocument(sample, sampleFile);
	}

	private void createPPTxDocument(String from, File file) throws Exception {

		file = new File(Thread.currentThread().getClass().getResource("poi/template.pptx").toURI());
		FileOutputStream out = new FileOutputStream(file);
		out.close();

	}

}