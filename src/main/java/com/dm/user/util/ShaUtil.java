package com.dm.user.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtil {

	/**
	 * 文件转换为byte
	 * @param path
	 * @return
	 */
	public static byte[] getFileByte(String path) {
		try {
			File file = new File(path);
			FileInputStream inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			return buffer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * sha 加密
     * @param strText
     * @return
     */
	public static String SHA(final byte[] strText) {
		String strResult = null;
		if (strText != null) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.update(strText);
				byte byteBuffer[] = messageDigest.digest();
				StringBuffer strHexString = new StringBuffer();
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return strResult;
	}

	public static String getStarString(String content, int begin, int end) {

		if (begin >= content.length() || begin < 0) {
			return content;
		}
		if (end >= content.length() || end < 0) {
			return content;
		}
		if (begin >= end) {
			return content;
		}
		String starStr = "";
		for (int i = begin; i < end; i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, begin) + starStr + content.substring(end, content.length());

	}

}
