package com.dinkydetails.weatherpull;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class WeatherService extends IntentService{

	 public static final String MESSENGER_KEY = "messenger";
	 public static final String TIME_KEY = "time";
	
	public WeatherService() {
		super("WeatherService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("onHandleIntent", "started");
		
		Bundle extras = intent.getExtras();
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		String timer = extras.getString(TIME_KEY);
		
		
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
	}

}
