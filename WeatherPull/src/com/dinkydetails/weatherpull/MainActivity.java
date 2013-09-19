// Dan Annis
// JAVA 2 Week 2
// CONTENT PROVIDER
// **** NOT EASY!! **** //

package com.dinkydetails.weatherpull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.Uri;
import android.os.*;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

@SuppressLint({ "HandlerLeak", "HandlerLeak" })
public class MainActivity extends Activity implements View.OnClickListener {

	//Listview adapter setup
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	//Setting up vars for main UI
	private EditText editTextZipCode;
	private Button buttonSearch;
	private TextView resultTextView;
	private Button btnFiveDays;
	private Button btnTenDays;

	///Other Vars
	private SQLiteDatabase db;
	//static final String baseURL = "api.wunderground.com/api/a988d453ebe759ad/forecast10day/q/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//accessing my UI components
		resultTextView = (TextView) findViewById(R.id.resultTextView);
		editTextZipCode = (EditText) findViewById(R.id.editTextZipCode);
		
		//Checking for activity on the edit button
		editTextZipCode
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							searchWeather(editTextZipCode.getText().toString()
									.trim(), 1);
							return true;
						}
						return false;
					}
				});
		//Setting up the buttons and their locations
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		buttonSearch.setOnClickListener(this);
		btnFiveDays = (Button) findViewById(R.id.btnfive);
		btnTenDays = (Button) findViewById(R.id.btnten);
		btnFiveDays.setOnClickListener(this);
		btnTenDays.setOnClickListener(this);
	
		//Listview setup
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		
		//helper to access the DB
		MyDataBaseHelper helper = new MyDataBaseHelper(this);
		db = helper.getWritableDatabase();

	}

	@Override
	//Setting up the buttons Click event cases
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSearch:
			db.rawQuery("Delete from " + WeatherDb.SQLITE_TABLE, null);
			resultTextView.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.GONE);
			if (editTextZipCode.getText().toString().length() == 0) {
				Toast.makeText(this, "Please Enter a Valid Zip Code!",
						Toast.LENGTH_LONG).show();
				return;
			}
			searchWeather(editTextZipCode.getText().toString().trim(), 1);
			break;

		case R.id.btnfive:
			db.rawQuery("Delete from " + WeatherDb.SQLITE_TABLE, null);
			resultTextView.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.GONE);
			if (editTextZipCode.getText().toString().length() == 0) {
				Toast.makeText(this, "Please Enter a Valid Zip Code!",
						Toast.LENGTH_LONG).show();
				return;
			}
			searchWeather(editTextZipCode.getText().toString().trim(), 2);
			break;

		case R.id.btnten:
			db.rawQuery("Delete from " + WeatherDb.SQLITE_TABLE, null);
			resultTextView.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.GONE);
			if (editTextZipCode.getText().toString().length() == 0) {
				Toast.makeText(this, "Please Enter a Valid Zip Code!",
						Toast.LENGTH_LONG).show();
				return;
			}
			searchWeather(editTextZipCode.getText().toString().trim(), 3);
			break;
		}

	}
	// 
	private void searchWeather(final String search_string, final int i) {
		//Close Keyboard after Entry
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextZipCode.getWindowToken(), 0);

		Handler myDataHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.i("SERVICE", "handlemessage() called");
				String response = null;

				if (msg.arg1 == RESULT_OK && msg.obj != null) {
					try {
						// search_string may be ZipCode
						response = (String) msg.obj;
						Log.i("JSON DATA", response);
						prepareListData(i);
					} catch (Exception e) {
						Log.e("JSON Response", e.toString());
					}
				}
			}
		};
		//Setting up Messenger to the Service
		Messenger myDataMessenger = new Messenger(myDataHandler);

		Intent startDataServiceIntent = new Intent(getBaseContext(),
				WeatherService.class);
		startDataServiceIntent.putExtra("messenger", myDataMessenger);
		startDataServiceIntent.putExtra("user_input", search_string);
		startService(startDataServiceIntent);
		//Show that the service is started and waiting information
		resultTextView.setText("Waiting....");
	}

	
	//Fetching the data from local DB and displaying it in list 
	public void displayData(String request) {
		String[] data = { WeatherDb.KEY_ROWID, WeatherDb.KEY_WEEKDAY,
				WeatherDb.KEY_FAHRENHEIT, WeatherDb.KEY_SKYICON };
		Uri uri = Uri.parse(myContentProvider.CONTENT_URI + "/" + 1);
		Cursor cursor = getContentResolver().query(uri, data, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			cursor.moveToNext();
			String weekday = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
			String fahrenheit = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
			String skyicon = cursor.getString(cursor
					.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

			StringBuffer msg = new StringBuffer();
			msg.append("City = ").append(weekday).append("\n");
			msg.append("Fahrenheit = ").append(fahrenheit).append("\n");
			msg.append("Skyicon = ").append(skyicon).append("\n");
			resultTextView.setText(msg);
		}
	}
	//Setting up the list to be ready for all the data that is going to go in it
	private void prepareListData(int i) {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		expListView.setAdapter(listAdapter);
		
		//Looking for the data to be put in
		Cursor cursor = db.rawQuery("SELECT * FROM " + WeatherDb.SQLITE_TABLE,
				null);

		resultTextView.setVisibility(View.GONE);
		expListView.setVisibility(View.VISIBLE);
		if (cursor != null && cursor.moveToFirst()) {
			
			//This looks for the results of the first search and puts the data into the header/row
			if (i == 1) {
				String weekday1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));
				//Set the Header to the day of the week
				listDataHeader.add(weekday1);
				//Put the contents into the row
				List<String> period1 = new ArrayList<String>();
				period1.add("Temprature = " + fahrenheit1 + " F");
				period1.add("Weather Details = " + skyicon1);

				listDataChild.put(listDataHeader.get(0), period1);
				//This looks for the results of the second search and puts the data into the header/row
			} else if (i == 2) {
				String weekday1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday2 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit2 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon2 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday3 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit3 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon3 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday4 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit4 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon4 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday5 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit5 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon5 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				listDataHeader.add(weekday1);
				listDataHeader.add(weekday2);
				listDataHeader.add(weekday3);
				listDataHeader.add(weekday4);
				listDataHeader.add(weekday5);

				List<String> period1 = new ArrayList<String>();
				period1.add("Temprature = " + fahrenheit1 + " F");
				period1.add("Weather Details = " + skyicon1);

				List<String> period2 = new ArrayList<String>();
				period2.add("Temprature = " + fahrenheit2 + " F");
				period2.add("Weather Details = " + skyicon2);

				List<String> period3 = new ArrayList<String>();
				period3.add("Temprature = " + fahrenheit3 + " F");
				period3.add("Weather Details = " + skyicon3);

				List<String> period4 = new ArrayList<String>();
				period4.add("Temprature = " + fahrenheit4 + " F");
				period4.add("Weather Details = " + skyicon4);

				List<String> period5 = new ArrayList<String>();
				period5.add("Temprature = " + fahrenheit5 + " F");
				period5.add("Weather Details = " + skyicon5);

				listDataChild.put(listDataHeader.get(0), period1);
				listDataChild.put(listDataHeader.get(1), period2);
				listDataChild.put(listDataHeader.get(2), period3);
				listDataChild.put(listDataHeader.get(3), period4);
				listDataChild.put(listDataHeader.get(4), period5);
				//This looks for the results of the third search and puts the data into the header/row
				//I'm thinking there is an easier way to do this! Just not sure how.. and not sure I have time. 
			
			} else if (i == 3) {
				String weekday1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday2 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit2 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon2 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday3 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit3 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon3 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday4 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit4 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon4 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday5 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit5 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon5 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday6 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit6 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon6 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday7 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit7 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon7 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				cursor.moveToNext();
				String weekday8 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit8 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon8 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				listDataHeader.add(weekday1);
				listDataHeader.add(weekday2);
				listDataHeader.add(weekday3);
				listDataHeader.add(weekday4);
				listDataHeader.add(weekday5);
				listDataHeader.add(weekday6);
				listDataHeader.add(weekday7);
				listDataHeader.add(weekday8);

				List<String> period1 = new ArrayList<String>();
				period1.add("Temprature = " + fahrenheit1 + " F");
				period1.add("Weather Details = " + skyicon1);

				List<String> period2 = new ArrayList<String>();
				period2.add("Temprature = " + fahrenheit2 + " F");
				period2.add("Weather Details = " + skyicon2);

				List<String> period3 = new ArrayList<String>();
				period3.add("Temprature = " + fahrenheit3 + " F");
				period3.add("Weather Details = " + skyicon3);

				List<String> period4 = new ArrayList<String>();
				period4.add("Temprature = " + fahrenheit4 + " F");
				period4.add("Weather Details = " + skyicon4);

				List<String> period5 = new ArrayList<String>();
				period5.add("Temprature = " + fahrenheit5 + " F");
				period5.add("Weather Details = " + skyicon5);

				List<String> period6 = new ArrayList<String>();
				period6.add("Temprature = " + fahrenheit6 + " F");
				period6.add("Weather Details = " + skyicon6);

				List<String> period7 = new ArrayList<String>();
				period7.add("Temprature = " + fahrenheit7 + " F");
				period7.add("Weather Details = " + skyicon7);

				List<String> period8 = new ArrayList<String>();
				period8.add("Temprature = " + fahrenheit8 + " F");
				period8.add("Weather Details = " + skyicon8);

				listDataChild.put(listDataHeader.get(0), period1);
				listDataChild.put(listDataHeader.get(1), period2);
				listDataChild.put(listDataHeader.get(2), period3);
				listDataChild.put(listDataHeader.get(3), period4);
				listDataChild.put(listDataHeader.get(4), period5);
				listDataChild.put(listDataHeader.get(5), period6);
				listDataChild.put(listDataHeader.get(6), period7);
				listDataChild.put(listDataHeader.get(7), period8);
			}

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
