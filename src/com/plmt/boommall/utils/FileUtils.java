package com.plmt.boommall.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;
import android.util.Log;

public class FileUtils {

	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/boommall";

	public static void makeDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			Log.i("error:", e + "");
		}
	}

	public static File createSDFile(String fileName) throws IOException {
		File file = new File(SD_PATH + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	public static void writeFileSdcard(String fileName, String message) {
		try {
			// FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
			FileOutputStream fout = new FileOutputStream(SD_PATH + fileName);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFileSdcard(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(SD_PATH + fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
