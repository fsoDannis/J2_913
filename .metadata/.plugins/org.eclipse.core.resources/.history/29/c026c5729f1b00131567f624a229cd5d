package com.dinkydetails.weatherpull;

import java.io.*;

import android.os.*;
import android.util.*;

public class DataStorage {

	private static final String TAG = DataStorage.class.getSimpleName();

	public static boolean storeStringFile(String filename, String message) {
		File dir = new File(Environment.getExternalStorageDirectory(), "/download");
		dir.mkdirs();
		File file = new File(dir, filename);
		if (file.exists()) {
			file.delete();
		}

		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(message.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return false;
		}

		return true;
	}
	
	public static String readStringFile(String filename) {
		FileInputStream fis = null;
		StringBuffer fileContent = new StringBuffer("");
		try {
			fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/download/" + filename);
			byte[] buffer = new byte[1024];
			while (fis.read(buffer) != -1) {
			    fileContent.append(new String(buffer));
			}
			fis.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return fileContent.toString();
	}

}
