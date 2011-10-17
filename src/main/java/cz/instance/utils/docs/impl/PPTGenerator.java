package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import cz.instance.utils.docs.DocumentGenerator;


public class PPTGenerator extends DocumentGenerator {

	@Override
	protected void populate(File file, String content) throws Exception {
		createPPTDocument(content, file);
	}

	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createPPTDocument(sample, sampleFile);
	}
       
	private void createPPTDocument(String from, File file) throws Exception {

	      SlideShow ppt = new SlideShow();

	      Slide slide = ppt.createSlide();

	      TextBox shape = new TextBox();
	      RichTextRun rt = shape.getTextRun().getRichTextRuns()[0];
	      shape.setText(from);
	      rt.setFontSize(7);
	      slide.addShape(shape);

	      shape.setAnchor(new java.awt.Rectangle(50, 50, 500, 300));  //position of the text box in the slide
	      slide.addShape(shape);


	      FileOutputStream out = new FileOutputStream(file);
	      ppt.write(out);
	      out.close();
	}
	
}
