package com.dinkydetails.weatherpull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class WeatherService extends IntentService{

	 public static final String MESSENGER_KEY = "messenger";
	 public static final String SEARCH_STRING = "user_input";
	 
	 Messenger messenger;
	 Message message;
	 String search_string;
	 
	public WeatherService() {
		super("WeatherService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String baseURL = "http://api.wunderground.com/api/a988d453ebe759ad/conditions/";
		String endURL = ".json";
		 URL finalURL;
		
		Log.i("onHandleIntent", "started");
		
		Bundle extras = intent.getExtras();
		messenger = (Messenger)extras.get(MESSENGER_KEY);
		search_string = (String)extras.getString(SEARCH_STRING);
		
		//11:27 Google Hangout
		 try {
		search_string = URLEncoder.encode(search_string, "UTF-8");
		 }catch (Exception e){
		Log.e ("Bad URL", "Encoding Problem");
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo =connMgr.getActiveNetworkInfo();
		
		if(networkInfo !=null && networkInfo.isConnected()){
			try{
				finalURL = new URL(baseURL + search_string + endURL);
				Log.i("URL",finalURL.toString());
				String response= "";
				
				URLConnection conn = finalURL.openConnection();
				BufferedInputStream bin= new BufferedInputStream(conn.getInputStream());
				
				byte[] contentBytes = new byte[1024];
				int bytesRead = 0;
				StringBuffer responseBuffer = new StringBuffer();
				
				while((bytesRead = bin.read(contentBytes)) != -1){
					response = new String(contentBytes,0,bytesRead);
					responseBuffer.append(response);
				}
				
				response = responseBuffer.toString();
				Log.i("JSON DATA", response);
				
				URI[] uri = new URI[1];
				try{
					uri[0] = newURI(finalURL.toString());
				}catch (URISyntaxException e){
					e.printStackTrace();
				}
				
				JSONObject json = null;
				try{
					json=new JSONObject(response);
					Log.i("JSON RESPONSE", "Valid JSON response");
				}catch (JSONException e){
					Log.e("ERROR StoreJSON", uri.toString());
					Log.e("ERROR StoreJSON", e.getMessage().toString());
				}
				
				if (json != null){
					DataStorage.storeStringFile(getBaseContext(), "my_data.txt",json.toString());
				}
				
			} catch (MalformedURLException e){
				Log.e("BAD URL", "Malformed URL");
				finalURL = null;
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "No Network Detected", Toast.LENGTH_SHORT);
			toast.show();
			}
		
		try{
			message = Message.obtain();
			message.arg1 = Activity.RESULT_OK;
			message.obj = "Data Service Complete";
			messenger.send(message);
			
			Log.d("Service Class", "onHandleIntent()");
			
		}catch (RemoteException e){
			Log.e("Exception On Handle Intent", e.getMessage().toString());
			e.printStackTrace();
		}
	}
				
				
/*				
		int countdown = 10;
		try{
			countdown = Integer.parseInt(timer);
		}catch (Exception e){
			Log.e("onHandleIntent", e.getMessage().toString());
		}
		
		while (countdown > 0)
		{
			try{
				Thread.sleep(1000);
			}catch (InterruptedException e){
				Log.e("Sleep", e.getMessage().toString());
				e.printStackTrace();
			}
			countdown --;
			
			Log.i("onHandleIntent", "Counter= "+ String.valueOf(countdown));
		}
		Log.i("onHandleIntent", "Counter is Done");
		
		Message message= Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		message.obj ="Service is Done";
		
		try {
			messenger.send(message);
		} catch (RemoteException e) {
			Log.e("onHandleIntent", e.getMessage().toString());
			e.printStackTrace();
		}
	}*/

}
