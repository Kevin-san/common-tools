package com.common.tools.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.common.tools.cons.Constant;
import com.common.tools.cons.ConstantExp;
import com.common.tools.ex.ToolsException;


public class FileToolUtils {
	private static Map<String, String> FILE_TYPE_MAP = new HashMap<>();
	private static Logger log = Logger.getLogger(FileToolUtils.class);

	private FileToolUtils() {
	}

	static {
		getAllFileType(); // 初始化文件类型信息
	}

	private static void getAllFileType() {
		FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg"); // JPEG (jpg)
		FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); // PNG (png)
		FILE_TYPE_MAP.put("47494638396126026f01", "gif"); // GIF (gif)
		FILE_TYPE_MAP.put("49492a00227105008037", "tif"); // TIFF (tif)
		FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
		FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); // 24位位图(bmp)
		FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
		FILE_TYPE_MAP.put("41433130313500000000", "dwg"); // CAD (dwg)
		FILE_TYPE_MAP.put("3c21444f435459504520", "html"); // HTML (html)
		FILE_TYPE_MAP.put("3c21646f637479706520", "htm"); // HTM (htm)
		FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); // css
		FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); // js
		FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
		FILE_TYPE_MAP.put("38425053000100000000", "psd"); // Photoshop (psd)
		FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
		FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); // MS Excel 注意：word、msi 和 excel的文件头一样
		FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "vsd"); // Visio 绘图
		FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
		FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
		FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); // Adobe Acrobat (pdf)
		FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
		FILE_TYPE_MAP.put("464c5601050000000900", "flv"); // flv与f4v相同
		FILE_TYPE_MAP.put("00000020667479706d70", "mp4");
		FILE_TYPE_MAP.put("49443303000000002176", "mp3");
		FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //
		FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
		FILE_TYPE_MAP.put("52494646e27807005741", "wav"); // Wave (wav)
		FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
		FILE_TYPE_MAP.put("4d546864000000060001", "mid"); // MIDI (mid)
		FILE_TYPE_MAP.put("504b0304140000000800", "zip");
		FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");
		FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
		FILE_TYPE_MAP.put("504b03040a0000000000", "jar");
		FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");// 可执行文件
		FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");// jsp文件
		FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");// MF文件
		FILE_TYPE_MAP.put("3c3f786d6c2076657273", "xml");// xml文件
		FILE_TYPE_MAP.put("494e5345525420494e54", "sql");// xml文件
		FILE_TYPE_MAP.put("7061636b61676520636f", "java");// java文件
		FILE_TYPE_MAP.put("406563686f206f66660d", "bat");// bat文件
		FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");// gz文件
		FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");// bat文件
		FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");// bat文件
		FILE_TYPE_MAP.put("49545346030000006000", "chm");// bat文件
		FILE_TYPE_MAP.put("04000000010000001300", "mxp");// bat文件
		FILE_TYPE_MAP.put("504b0304140006000800", "docx");// docx文件
		FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "wps");// WPS文字wps、表格et、演示dps都是一样的
		FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
		FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
		FILE_TYPE_MAP.put("FF575043", "wpd"); // WordPerfect (wpd)
		FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
		FILE_TYPE_MAP.put("2142444E", "pst"); // Outlook (pst)
		FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); // Quicken (qdf)
		FILE_TYPE_MAP.put("E3828596", "pwl"); // Windows Password (pwl)
		FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)

	}

	/**
	 * 
	 * @param src
	 * @return
	 **/
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (null == src || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileType(String fileName) {
		String res = null;
		try (FileInputStream fis = new FileInputStream(fileName);) {
			byte[] b = new byte[10];
			fis.read(b, 0, b.length);
			String fileCode = bytesToHexString(b);
			log.info(fileCode);
			Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = keyIter.next(); // 比较前几位是否相同就可以判断文件格式（相同格式文件文件头后面几位会有所变化）
				if (fileCode!=null&&(key.toLowerCase().startsWith(fileCode.toLowerCase())
						|| fileCode.toLowerCase().startsWith(key.toLowerCase()))) {
					res = FILE_TYPE_MAP.get(key);
					break;
				}
			}
			log.info("文件头:" + fileCode + "-----文件类型:" + res);
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
		}
		return res;
	}

	public static byte[] image2byte(String path) {
		byte[] data = null;
		try (FileImageInputStream input = new FileImageInputStream(new File(path));
				ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
		}
		return data;
	}

	public static void byte2Image(byte[] data, String path) {
		if (data.length < 3 || Constant.EMPTY.equals(path)) {
			return;
		}
		try (FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));) {
			imageOutput.write(data, 0, data.length);
			log.info("Make Picture Success,please find image in "+ path);
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
		}
	}

	public static String byteTo16String(byte[] data) {
		if (data == null || data.length <= 1 || data.length > 200000) {
			return "0x";
		}
		StringBuilder sb = new StringBuilder();
		int[] buf = new int[data.length];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = data[i] < 0 ? (data[i] + 256) : data[i];
		}
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] < 16) {
				sb.append("0" + Integer.toHexString(buf[i]));
			} else {
				sb.append(Integer.toHexString(buf[i]));
			}
		}
		return sb.toString().toUpperCase();
	}

	public static String byte2HexStr(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String stmp = (Integer.toHexString(b[i] & 0XFF));
			if (stmp.length() == 1) {
				sb.append("0" + stmp); 
			} else {
				sb.append(stmp);
			}
		}
		return sb.toString().toUpperCase();
	}

	public static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		return (byte) (b0 | b1);
	}

	public static int charToInt(byte ch) {
		int val = 0;
		if (ch >= 0x30 && ch <= 0x39) {
			val = ch - 0x30;
		} else if (ch >= 0x41 && ch <= 0x46) {
			val = ch - 0x41 + 10;
		}
		return val;
	}

	public static void img2String(String fileUrl, String imagePath) {
		try (FileInputStream fis = new FileInputStream(imagePath);
				BufferedInputStream imis = new BufferedInputStream(fis);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PrintWriter pw = new PrintWriter(new FileWriter(fileUrl));) {
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			byte[] result = bos.toByteArray();
			String str = byte2HexStr(result);
			pw.println(str);
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
		}
	}
	public static void saveToImage(String src,String  output) {
		if (StringUtils.isEmpty(src)) {
			return;
		}
		try(FileOutputStream out=new FileOutputStream(new File(output));) {
			byte[] bytes=src.getBytes();
			for (int i = 0; i < bytes.length; i+=2) {
				out.write(charToInt(bytes[i])*16+charToInt(bytes[i+1]));
			}
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
		}
	}
	public static void string2Img(String path,String fileUrl) throws ToolsException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));){
			String str=null;
			StringBuilder sb = new StringBuilder();
			while ((str=br.readLine())!=null) {
				sb.append(str);
			}
			saveToImage(sb.toString().toUpperCase(), fileUrl);
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}
	
	public static void close(BufferedReader reader) throws ToolsException {
		try {
			reader.close();
		} catch (IOException e) {
			log.error(ConstantExp.IO_Exception, e);
			throw new ToolsException(e);
		}
	}

}
