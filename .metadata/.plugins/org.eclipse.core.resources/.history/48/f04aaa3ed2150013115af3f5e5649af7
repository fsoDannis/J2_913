package com.dinkydetails.weatherpull;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class WeatherService extends IntentService{

	public WeatherService() {
		super("WeatherService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		int countdown = 10;
		
		while (countdown > 0)
		{
			try{
				Thread.sleep(1000);
			}catch (InterruptedException e){
				Log.e("Sleep", e.getMessage().toString());
				e.printStackTrace();
				
			}
		}
		
	}

}
