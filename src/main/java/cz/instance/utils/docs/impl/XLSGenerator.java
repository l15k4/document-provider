package cz.instance.utils.docs.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cz.instance.utils.docs.DocumentGenerator;


public class XLSGenerator extends DocumentGenerator {


	@Override
	protected void populate(File file, String content) throws Exception {
		createXLSDocument(content, file);
	}


	@Override
	protected void populateSample(File sampleFile, String sample) throws Exception {
		createXLSDocument(sample, sampleFile);
	}

	private void createXLSDocument(String from, File file) throws Exception {
       //EXCEL 97 max string length in cell is 32,767
		
		if(from.length() > 32767)
			from = from.substring(0, 32766);
		
	    Workbook wb = new HSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();

	    CellStyle hlink_style = wb.createCellStyle();
	    Font hlink_font = wb.createFont();
	    hlink_font.setUnderline(Font.U_SINGLE);
	    hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);

	    Cell cell;
	    Sheet sheet = wb.createSheet("Hyperlinks");
	    //URL
	    cell = sheet.createRow(0).createCell((short)0);
	    cell.setCellValue("URL Link");

	    Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
	    link.setAddress("http://poi.apache.org/");
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);

	    //link to a file in the current directory
	    cell = sheet.createRow(1).createCell((short)0);
	    cell.setCellValue("File Link");
	    link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
	    link.setAddress("link1.xls");
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);

	    //e-mail link
	    cell = sheet.createRow(2).createCell((short)0);
	    cell.setCellValue("Email Link");
	    link = createHelper.createHyperlink(Hyperlink.LINK_EMAIL);
	    //note, if subject contains white spaces, make sure they are url-encoded
	    link.setAddress("mailto:poi@apache.org?subject=Hyperlinks");
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);

	    //link to a place in this workbook

	    //create a target sheet and cell
	    Sheet sheet2 = wb.createSheet("Target Sheet");
	    sheet2.createRow(0).createCell((short)0).setCellValue("Target Cell");

	    cell = sheet.createRow(3).createCell((short)0);
	    cell.setCellValue(from);

		FileOutputStream fos = new FileOutputStream(file);
		// document.write(new PrintStream(new FileOutputStream(file), false,
		// "UTF-8"));
		wb.write(fos);
		
	}
	
}
