package com.dinkydetails.weatherpull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LM_Fragment extends Fragment implements OnClickListener {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	private EditText editTextZipCode;
	private Button buttonSearch;
	private TextView resultTextView;

	private Button btnFiveDays;
	private Button btnTenDays;
	private Button btnNext;

	
	private SQLiteDatabase db;

	String zipCode = null;
	private boolean clickFlag = false;

	static final String baseURL = "api.wunderground.com/api/a988d453ebe759ad/forecast10day/q/";

	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/**
		 * Inflate the layout for this fragment
		 */

		View view = inflater.inflate(R.layout.lm_fragment, container, false);
		context = view.getContext();
		resultTextView = (TextView) view.findViewById(R.id.resultTextView);
		editTextZipCode = (EditText) view.findViewById(R.id.editTextZipCode);

		// Creating reference for all the buttons
		buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
		buttonSearch.setOnClickListener(this);

		btnFiveDays = (Button) view.findViewById(R.id.btnfive);
		btnTenDays = (Button) view.findViewById(R.id.btnten);
		btnNext = (Button) view.findViewById(R.id.btnnext);

		btnFiveDays.setOnClickListener(this);
		btnTenDays.setOnClickListener(this);
		btnNext.setOnClickListener(this);	

		expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

		// Database helper
		MyDataBaseHelper helper = new MyDataBaseHelper(view.getContext());
		db = helper.getInstance(view.getContext()).getWritableDatabase();

		if(MainActivity.nClickedBtn!=0){
			prepareListData(MainActivity.nClickedBtn);
			editTextZipCode.setText(""+MainActivity.ZipCode);
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		resultTextView.setVisibility(View.VISIBLE);
		Bundle params = ((Activity) context).getIntent().getExtras();
		if (params != null) {
			String maxWind = params.getString("maxWind");
			String avgWind = params.getString("avgWind");
			String maxHumidity = params.getString("maxHumidity");
			String zip = params.getString("zip");
			editTextZipCode.setText(zip);
			resultTextView
					.setText("The data received from previous Activity through intents is \n\nAverage Wind : "
							+ avgWind
							+ ", Max Wind : "
							+ maxWind
							+ " and Max Humidity : " + maxHumidity);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSearch:
			clickFlag = true;
			db.delete(WeatherDb.SQLITE_TABLE, null, null);
			resultTextView.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.GONE);
			if (editTextZipCode.getText().toString().length() == 0) {
				Toast.makeText(context, "Please Enter a Valid Zip Code!",
						Toast.LENGTH_LONG).show();
				return;
			} else {
				MainActivity.ZipCode=editTextZipCode.getText().toString().trim();
				searchWeather(editTextZipCode.getText().toString().trim(), 1);
			}
			break;

		case R.id.btnfive:
			clickFlag = true;
			db.delete(WeatherDb.SQLITE_TABLE, null, null);
			resultTextView.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.GONE);
			if (editTextZipCode.getText().toString().length() == 0) {
				Toast.makeText(context, "Please Enter a Valid Zip Code!",
						Toast.LENGTH_LONG).show();
				return;
			} else {
				MainActivity.ZipCode = editTextZipCode.getText().toString()
						.trim();
				searchWeather(editTextZipCode.getText().toString().trim(), 2);
			}
			break;

		case R.id.btnten:
			clickFlag = true;
			db.delete(WeatherDb.SQLITE_TABLE, null, null);
			resultTextView.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.GONE);
			if (editTextZipCode.getText().toString().length() == 0) {
				Toast.makeText(context, "Please Enter a Valid Zip Code!",
						Toast.LENGTH_LONG).show();
				return;
			} else {
				MainActivity.ZipCode = editTextZipCode.getText().toString()
						.trim();
				searchWeather(editTextZipCode.getText().toString().trim(), 3);
			}
			break;

		case R.id.btnnext:
			if (clickFlag == true) {
				if (editTextZipCode.getText().toString().length() == 0) {
					Toast.makeText(context, "Please Enter a Valid Zip Code!",
							Toast.LENGTH_LONG).show();
					return;
				} else {
					Intent iNextIntent = new Intent(context,
							SecondActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("zip", editTextZipCode.getText()
							.toString().trim());
					iNextIntent.putExtras(bundle);
					startActivityForResult(iNextIntent, 0);

				}
			} else {
				Toast.makeText(context, "Please search the data first.",
						Toast.LENGTH_LONG).show();
			}
			break;
		}

	}

	private void searchWeather(final String search_string, final int i) {
		final InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextZipCode.getWindowToken(), 0);

		// Service Handler
		Handler myDataHandler = new Handler() {
			@SuppressWarnings("static-access")
			@Override
			public void handleMessage(Message msg) {
				Log.i("SERVICE", "handlemessage() called");
				String response = null;

				if (msg.arg1 == ((Activity)context).RESULT_OK && msg.obj != null) {
					try {
						response = (String) msg.obj;
						Log.i("JSON DATA", response);

						prepareListData(i);
					} catch (Exception e) {
						Log.e("JSON Response", e.toString());
					}
				}
			}
		};

		Messenger myDataMessenger = new Messenger(myDataHandler);

		Intent startDataServiceIntent = new Intent(context,
				WeatherService.class);
		startDataServiceIntent.putExtra("messenger", myDataMessenger);
		startDataServiceIntent.putExtra("user_input", search_string);
		context.startService(startDataServiceIntent);
		resultTextView.setText("Waiting....");
	}

	public void displayData(String request) {
		String[] data = { WeatherDb.KEY_ROWID, WeatherDb.KEY_WEEKDAY,
				WeatherDb.KEY_FAHRENHEIT, WeatherDb.KEY_SKYICON };
		Uri uri = Uri.parse(myContentProvider.CONTENT_URI + "/" + 1);
		Cursor cursor = context.getContentResolver().query(uri, data, null,
				null, null);
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

	public void prepareListData(int i) {
		MainActivity.nClickedBtn = i;
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		listAdapter = new ExpandableListAdapter(context, listDataHeader,
				listDataChild);
		expListView.setAdapter(listAdapter);

		Cursor cursor = db.rawQuery("SELECT * FROM " + WeatherDb.SQLITE_TABLE,
				null);

		resultTextView.setVisibility(View.GONE);
		expListView.setVisibility(View.VISIBLE);
		if (cursor != null && cursor.moveToFirst()) {

			// Checking which button click needs to be handled
			if (i == 1) {
				String weekday1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_WEEKDAY));
				String fahrenheit1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_FAHRENHEIT));
				String skyicon1 = cursor.getString(cursor
						.getColumnIndexOrThrow(WeatherDb.KEY_SKYICON));

				listDataHeader.add(weekday1);

				List<String> period1 = new ArrayList<String>();
				period1.add("Temprature = " + fahrenheit1 + " F");
				period1.add("Weather Details = " + skyicon1);

				listDataChild.put(listDataHeader.get(0), period1);

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

		} else {
			resultTextView.setVisibility(View.VISIBLE);
			resultTextView.setText("No cities match your search query");
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		zipCode = editTextZipCode.getText().toString().trim();
		super.onSaveInstanceState(outState);
		outState.putSerializable("clickedBtn", MainActivity.nClickedBtn);
	}

	
	
}