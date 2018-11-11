package com.common.tools.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.common.tools.cons.Constant;
import com.common.tools.cons.ConstantExp;
import com.common.tools.cons.FileMapConstant;
import com.common.tools.ex.ToolsException;
import com.common.tools.util.CommonUtils;
import com.common.tools.util.FileToolUtils;
import com.common.tools.util.FormatUtils;

public class CsvReader {
	private static Logger logger = Logger.getLogger(CsvReader.class);

	private String fileName;
	private String encoding;
	private String splitChar;
	private List<Integer> indexes = new ArrayList<>();
	private BufferedReader reader;

	public CsvReader(String fileName, String encoding, String splitChar, List<Integer> indexes) {
		try {
			this.fileName = fileName;
			this.encoding = encoding;
			this.splitChar = splitChar;
			this.indexes = indexes;
			this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		} catch (FileNotFoundException e) {
			logger.error(ConstantExp.FILE_NOT_FOUND_Exception, e);
		}
	}

	public String[] getCsvLine(String dataLine, String keyStr) throws ToolsException{
		if (FormatUtils.isOddQuotaCount(dataLine)) {
			logger.info("Invalid Csv format:" + dataLine);
			return new String[0];
		}
		String seperator = Constant.SPEC_MAPS_CONVS.containsKey(keyStr) ? Constant.SPEC_MAPS_CONVS.get(keyStr) : keyStr;
		if (FormatUtils.isZeroQuotaCount(dataLine)) {
			return dataLine.split(seperator);
		}
		return getSpecCsvLine(dataLine);
	}

	public String[] getSpecCsvLine(String dataLine) throws ToolsException {
		try {
			String[] dataArray = dataLine.split(splitChar);
			List<String> results = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < dataArray.length; i++) {
				String cell = new String(dataArray[i].getBytes(encoding));
				if (isValidQuatoCell(cell) || isValidCell(cell)) {
					results.add(replaceAllQuato(cell));
				} else {
					if (FormatUtils.isOddStartQuota(cell)) {
						sb.append(replaceAllQuato(cell + splitChar));
					} else if (FormatUtils.isOddEndQuota(cell)) {
						sb.append(replaceAllQuato(cell));
						results.add(sb.toString());
						sb = new StringBuilder();
					} else {
						sb.append(cell + splitChar);
					}
				}
			}
			return results.toArray(new String[results.size()]);
		} catch (UnsupportedEncodingException e) {
			logger.error(ConstantExp.UNSUPPORTEDENCODING_Exception,e);
			throw new ToolsException(e);
		}
	}

	public String replaceAllQuato(String cell) {
		return cell.replaceAll(Constant.QUOTA + Constant.BLANK, Constant.BLANK);
	}

	public boolean isValidCell(String cell) {
		return !FormatUtils.isOddStartQuota(cell) && !FormatUtils.isOddEndQuota(cell);
	}

	public boolean isValidQuatoCell(String cell) {
		return FormatUtils.isOddStartQuota(cell) && FormatUtils.isOddEndQuota(cell);
	}

	public boolean isCsvFile() {
		return FileMapConstant.CSV.equals(CommonUtils.getFileSuffix(fileName));
	}

	public String[] readLine() throws ToolsException {
		try {
			String line = reader.readLine();
			String[] csvValues=getCsvLine(line, splitChar);
			String[] result = new String[indexes.size()];
			for (int i = 0; i < indexes.size(); i++) {
				result[i]=csvValues[indexes.get(i)];
			}
			return result;
		} catch (IOException e) {
			logger.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}

	public void excludeHeader(int excludeRow) throws ToolsException {
		for (int i = 0; i < excludeRow; i++) {
			try {
				reader.readLine();
			} catch (IOException e) {
				logger.error(ConstantExp.IO_Exception, e);
				throw new ToolsException(e);
			}
		}
	}
	
	public void close() throws ToolsException {
		FileToolUtils.close(reader);
	}
}
