package cz.instance.utils.docs.impl;

import java.io.File;

import org.apache.commons.io.FileUtils;

import cz.instance.utils.docs.DocumentGenerator;

public class TXTGenerator extends DocumentGenerator {

	@Override
	protected void populate(File file, String content) throws Exception {
		FileUtils.writeStringToFile(file, content);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createSampleFile(sampleFile, sample);
	}
}
