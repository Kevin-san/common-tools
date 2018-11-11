package com.common.tools.file;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import com.common.tools.cons.ConstantExp;
import com.common.tools.ex.ToolsException;


public class PdfTextReader {
	private static Logger logger = Logger.getLogger(PdfTextReader.class);
	private PDDocument pdfDocument;

	public PdfTextReader(String pdfFileName, String password) throws ToolsException {
		try {
			this.pdfDocument = PDDocument.load(new File(pdfFileName), password);
		} catch (InvalidPasswordException e) {
			logger.error(ConstantExp.INVALIDPASSWORD_Exception, e);
			throw new ToolsException(e);
		} catch (IOException e) {
			logger.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}

	public int getPages() {
		return pdfDocument.getNumberOfPages();
	}

	public String readTextPage(int from, int to) throws ToolsException {
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			stripper.setStartPage(from);
			stripper.setEndPage(to);
			return stripper.getText(pdfDocument);
		} catch (IOException e) {
			logger.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}

	public String readTextPage(int pageIndex) throws ToolsException {
		return readTextPage(pageIndex, pageIndex);
	}

	public String getAuthor() {
		return pdfDocument.getDocumentInformation().getAuthor();
	}

	public String getSubject() {
		return pdfDocument.getDocumentInformation().getSubject();
	}

	public String getTitle() {
		return pdfDocument.getDocumentInformation().getTitle();
	}
	
	public void close() throws ToolsException {
		try {
			pdfDocument.close();
		} catch (IOException e) {
			logger.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}

}
