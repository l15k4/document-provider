package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

import cz.instance.utils.docs.DocumentGenerator;

public class HTMLGenerator extends DocumentGenerator {

	private HtmlCleaner cleaner = new HtmlCleaner();

	@Override
	protected void populate(File file, String content) throws Exception {
		List<String> paragraphs = getParagraphs(content);
		List<TagNode> pNodes = new ArrayList<TagNode>();

		TagNode html = new TagNode(htmlType);
		for (String paragraph : paragraphs) {
			TagNode p = new TagNode("p");
			p.addChild(new ContentNode(paragraph));
			pNodes.add(p);
		}
		html.addChildren(pNodes);
		CleanerProperties props = cleaner.getProperties();
		new SimpleHtmlSerializer(props).writeToStream(html, new FileOutputStream(file));
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		CleanerProperties props = cleaner.getProperties();
		TagNode sampleParagraph = new TagNode("p");
		sampleParagraph.addChild(new ContentNode(sample));
		new SimpleHtmlSerializer(props).writeToStream(sampleParagraph, new FileOutputStream(sampleFile));
	}
}
