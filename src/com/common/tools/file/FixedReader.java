package com.common.tools.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

import com.common.tools.cons.ConstantExp;
import com.common.tools.ex.ToolsException;
import com.common.tools.util.FileToolUtils;

public class FixedReader {
	private static Logger logger = Logger.getLogger(FixedReader.class);

	private String encoding;
	private List<Integer> indexes;
	private BufferedReader reader;

	public FixedReader(String fileName, String encoding, List<Integer> indexes) {
		try {
			this.encoding = encoding;
			this.indexes = indexes;
			this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		} catch (FileNotFoundException e) {
			logger.error(ConstantExp.FILE_NOT_FOUND_Exception, e);
		}
	}

	public byte[] copyOfRange(byte[] original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0)
			throw new IllegalArgumentException(from + " > " + to);
		byte[] copy = new byte[newLength];
		System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
		return copy;
	}

	public String[] readLine() throws ToolsException{
		try {
			byte[] original = reader.readLine().getBytes();
			int begin = 0;
			int end = 0;
			int lineLength = original.length;
			int length = indexes.size();
			String[] columnValues = new String[length];
			byte[][] byteArray = new byte[length][];
			for (int i = 0; i < length; i++) {
				columnValues[i] = "";
				if (begin < lineLength) {
					end += indexes.get(i);
					end = end > lineLength ? lineLength : end;
					byteArray[i] = copyOfRange(original, begin, end);
					columnValues[i] = new String(byteArray[i], encoding);
					columnValues[i] = columnValues[i].trim();
					begin = end;
				}
			}
			return columnValues;
		} catch (IOException e) {
			logger.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}
	public void close() throws ToolsException {
		FileToolUtils.close(reader);
	}
}
