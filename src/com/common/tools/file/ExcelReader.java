package com.common.tools.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.tools.cons.ConstantExp;
import com.common.tools.ex.ToolsException;

public class ExcelReader {
	private static Logger log = Logger.getLogger(ExcelReader.class);
	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";
	private Workbook wb;

	public ExcelReader(String fileName) throws ToolsException {
		try {
			FileInputStream in = new FileInputStream(fileName);
			if (fileName.endsWith(EXCEL_XLS)) {
				wb = new HSSFWorkbook(in);
			} else if (fileName.endsWith(EXCEL_XLSX)) {
				wb = new XSSFWorkbook(in);
			}
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}

	public void creatWorkSheet(String sheetName) {
		wb.createSheet(sheetName);
	}

	public Sheet getSheet(String sheetName) {
		return wb.getSheet(sheetName);
	}

	public Sheet getSheetAt(int index) {
		return wb.getSheetAt(index);
	}

	public int getSheetCnt() {
		return wb.getNumberOfSheets();
	}

	public List<? extends Name> getNames() {
		return wb.getAllNames();
	}

	public Cell getCellAt(String sheetName, int rowIndex, int columnIndex) {
		return getSheet(sheetName).getRow(rowIndex).getCell(columnIndex);
	}

	public Cell getCellAt(int sheetIndex, int rowIndex, int columnIndex) {
		return getSheetAt(sheetIndex).getRow(rowIndex).getCell(columnIndex);
	}

	public CellStyle getCellStyle(String sheetName, int rowIndex, int columnIndex) {
		return getCellAt(sheetName, rowIndex, columnIndex).getCellStyle();
	}

	public int getRowCnts(String sheetName) {
		return getSheet(sheetName).getPhysicalNumberOfRows();
	}

	public int getCellCnts(String sheetName, int rowIndex) {
		return getSheet(sheetName).getRow(rowIndex).getPhysicalNumberOfCells();
	}

	public Header getHeader(String sheetName) {
		return getSheet(sheetName).getHeader();
	}

	public Footer getFooter(String sheetName) {
		return getSheet(sheetName).getFooter();
	}

	public Cell getFirstCell(String sheetName, int rowIndex) {
		Row row = getSheet(sheetName).getRow(rowIndex);
		return row.getCell(row.getFirstCellNum());
	}

	public Cell getLastCell(String sheetName, int rowIndex) {
		Row row = getSheet(sheetName).getRow(rowIndex);
		return row.getCell(row.getLastCellNum());
	}

	public List<String[]> readSheet(int sheetIndex) {
		List<String[]> list = new ArrayList<>();
		Sheet sheet = getSheetAt(sheetIndex);
		if (sheet == null) {
			return list;
		}
		for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			String[] cells = new String[row.getPhysicalNumberOfCells()];
			for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
				Cell cell = row.getCell(cellIndex);
				cells[cellIndex] = getCellValue(cell);
			}
			list.add(cells);
		}
		return list;
	}

	public String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		// 把数字当成String来读，避免出现1读成1.0的情况
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			cell.setCellType(CellType.STRING);
		}
		// 判断数据的类型
		switch (cell.getCellTypeEnum()) {
		case NUMERIC: // 数字
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case STRING: // 字符串
			cellValue = String.valueOf(cell.getStringCellValue());
			break;
		case BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA: // 公式
			cellValue = String.valueOf(cell.getCellFormula());
			break;
		case BLANK: // 空值
			cellValue = "";
			break;
		case ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}
		return cellValue;
	}

	public Object getValue(Cell cell) {
		Object obj = null;
		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			obj = cell.getBooleanCellValue();
			break;
		case ERROR:
			obj = cell.getErrorCellValue();
			break;
		case NUMERIC:
			obj = cell.getNumericCellValue();
			break;
		case STRING:
			obj = cell.getStringCellValue();
			break;
		default:
			break;
		}
		return obj;
	}

	public void close() throws ToolsException {
		try {
			wb.close();
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}
}
