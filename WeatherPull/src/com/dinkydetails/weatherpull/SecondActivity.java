package com.dinkydetails.weatherpull;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends Activity implements OnClickListener {
	//Setting up my variables
	private TextView mHeading;
	private TextView mWeekday;
	private TextView mTemp;
	private TextView mSkyIcon;
	private TextView mMaxWind;
	private TextView mAvgWind;
	private TextView mMaxHumidity;
	
	//Setting up buttons
	private Button btnImplicit;
	private Button btnBack;
	
	//Other Variables
	String weekday;
	String fahrenheit;
	String skyicon;
	String maxWind;
	String avgWind;
	String maxHumidity;
	
	//access to the DB sort of
	private SQLiteDatabase db;
	
	//Intent will load the weather info from the chosen zip code and display all the details to this URL
	private String url = "http://api.wunderground.com/cgi-bin/findweather/hdfForecast?query=";
	private String zipCode = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		Bundle params = getIntent().getExtras();
		if (params != null) {
			zipCode = params.getString("zip");
		}

		url = url + zipCode;

		MyDataBaseHelper helper = new MyDataBaseHelper(this);
		db = helper.getInstance(this).getWritableDatabase();

		mHeading = (TextView) findViewById(R.id.textheading);
		mWeekday = (TextView) findViewById(R.id.textweekday);
		mTemp = (TextView) findViewById(R.id.texttemp);
		mSkyIcon = (TextView) findViewById(R.id.textskyicon);
		mMaxWind = (TextView) findViewById(R.id.textmaxwind);
		mAvgWind = (TextView) findViewById(R.id.textavgwind);
		mMaxHumidity = (TextView) findViewById(R.id.textmaxhumid);

		displayData();

		btnImplicit = (Button) findViewById(R.id.btnimplicit);
		btnBack = (Button) findViewById(R.id.btnback);

		btnImplicit.setOnClickListener(this);
		btnBack.setOnClickListener(this);

	}

	private void displayData() {
		Cursor cursor = db.rawQuery("SELECT * FROM " + WeatherDb.SQLITE_TABLE,
				null);
		if (cursor != null && cursor.moveToFirst()) {
			weekday = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
			fahrenheit = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
			skyicon = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));
			maxWind = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_MAXWIND));
			avgWind = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_AVGWIND));
			maxHumidity = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_MAXHUMIDITY));

			mHeading.setText("Today's Details for " + zipCode);
			mWeekday.setText("Weekday : " + weekday);
			mTemp.setText("Temprature : " + fahrenheit + " F");
			mSkyIcon.setText("Sky : " + skyicon);
			mMaxWind.setText("Max Wind : " + maxWind + " mph");
			mAvgWind.setText("Average Wind : " + avgWind + " mph");
			mMaxHumidity.setText("Max Humidity : " + maxHumidity);

		} else {
			mHeading.setText("No cities match your search query");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnimplicit:
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(i);
			break;

		case R.id.btnback:
			Intent iBackIntent = new Intent(SecondActivity.this,
					MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("maxWind", maxWind);
			bundle.putString("avgWind", avgWind);
			bundle.putString("maxHumidity", maxHumidity);
			bundle.putString("zip", zipCode);
			iBackIntent.putExtras(bundle);
			startActivity(iBackIntent);
			finish();
			break;
		}

	}

	public void finish() {
		super.finish();
		Intent data = new Intent();
		data.putExtra("maxWind", maxWind);
		data.putExtra("avgWind", avgWind);
		data.putExtra("maxHumidity", maxHumidity);
		setResult(RESULT_OK, data);
	}
}
