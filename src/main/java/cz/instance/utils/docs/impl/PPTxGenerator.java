package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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

		InputStream input = Thread.currentThread().getClass().getResourceAsStream("/poi/template.pptx");
		FileOutputStream out = new FileOutputStream(file);
		
		int b;
		while((b = input.read()) >= 0) {
			out.write(b);
		}
		out.close();
	}
}