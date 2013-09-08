package com.dinkydetails.weatherpull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class WeatherService extends IntentService{

	private int result = Activity.RESULT_CANCELED;
	public static final String URL_STRING = "finalURL";
	public static final String FILENAME = "filename";
	public static final String FILEPATH = "filepath";
	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.dinkydetails.android.service.receiver";


	public WeatherService() {
		super("WeatherService");
	}
	

	@Override
	protected void onHandleIntent(Intent intent) {
		
		String finalURL = intent.getStringExtra(URL_STRING);
		String fileName = intent.getStringExtra(FILENAME);
		
		File output = new File(Environment.getExternalStorageDirectory(),fileName);

		if (output.exists()) {
		output.delete();
		}

		InputStream stream = null;
		FileOutputStream fos = null;

		try {

		URL url = new URL(finalURL);
		
		stream = url.openConnection().getInputStream();
		InputStreamReader reader = new InputStreamReader(stream);
		// fos = new FileOutputStream(output.getPath());

		fos = openFileOutput(fileName, Context.MODE_PRIVATE);
		int next = -1;
		while ((next = reader.read()) != -1) {
			fos.write(next);

		}	

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		publishResults(output.getAbsolutePath(), result);

	}


	private void publishResults(String outputPath, int result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(FILEPATH, outputPath);
		intent.putExtra(RESULT, result);
		sendBroadcast(intent);
	}

}