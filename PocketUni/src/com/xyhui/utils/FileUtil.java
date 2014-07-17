package com.xyhui.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;

import com.mslibs.utils.VolleyLog;

public class FileUtil {
	public static final String PU_DIR = "pu";

	/**
	 * 将str写入文件保存
	 * 
	 * @param content
	 */
	public static void writeContent(String content, String fileName) {
		FileOutputStream fosToken;
		try {
			fosToken = new FileOutputStream(getFilename(fileName));
			fosToken.write(content.getBytes("UTF-8"));
			fosToken.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成保存token的文件
	 * 
	 * @return token.txt
	 */
	public static String getFilename(String fileName) {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, PU_DIR);

		if (!file.exists()) {
			file.mkdirs();
		}
		VolleyLog.d(file.getAbsolutePath() + "/" + fileName);
		return file.getAbsolutePath() + "/" + fileName;
	}
}
