package com.dinkydetails.weatherpull;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SecondActivity extends Activity implements OnClickListener {
	//Setting up my variables
	private Button btnImplicit;
	private Button btnBack;
	//Intent will load the weather info from the chosen zip code and display all the details to this URL
	private String url = "http://api.wunderground.com/cgi-bin/findweather/hdfForecast?query=";
	private String zipCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		//grabbing the zipcode from the previous screen
		Bundle params = getIntent().getExtras();
		zipCode = params.getString("zip");
		//adding the zipcode to the end of the URL
		url = url + zipCode;
		//tieing the buttons to their IDs	
		btnImplicit = (Button) findViewById(R.id.btnimplicit);
		btnBack = (Button) findViewById(R.id.btnback);
		// Setting up the listener
		btnImplicit.setOnClickListener(this);
		btnBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//When clicked, send the URL and open web explorer
		case R.id.btnimplicit:
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(i);
			break;
		// go back to the previous screen
		case R.id.btnback:
			Intent iBackIntent = new Intent(SecondActivity.this,
					MainActivity.class);
			startActivity(iBackIntent);
			break;
		}

	}

}
