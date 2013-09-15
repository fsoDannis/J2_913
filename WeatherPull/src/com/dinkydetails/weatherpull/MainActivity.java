//Dan Annis
//Sept. 2013


package com.dinkydetails.weatherpull;

import org.json.*;

import android.net.Uri;
import android.os.*;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements View.OnClickListener {
	
	//Variable's for UI
	private EditText editTextZipCode = null;
	private Button buttonSearch = null;
	private TextView resultTextView = null;
	
	//Other Declarations
	static final String baseURL = "api.wunderground.com/api/a988d453ebe759ad/forecast10day/q/";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		resultTextView = (TextView) findViewById(R.id.resultTextView);
		editTextZipCode = (EditText) findViewById(R.id.editTextZipCode);

		editTextZipCode
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							searchWeather(editTextZipCode.getText().toString()
									.trim());
							return true;
						}
						return false;
					}
				});
		//Simple Button with Listener
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		buttonSearch.setOnClickListener(this);
	}
		

	@Override
	public void onClick(View v) {
		if (editTextZipCode.getText().toString().length() == 0) {
			Toast.makeText(this, "Please Enter a Valid Zip Code!",
					Toast.LENGTH_LONG).show();
			return;
		}

		searchWeather(editTextZipCode.getText().toString().trim());

	}
	
	private void searchWeather(final String search_string) {
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
						response = (String) msg.obj;
						Log.i("JSON DATA", response);

						// search_string may be ZipCode
						displayData(search_string);

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
	
	
	
		
	/**
	 * Fetching the data from local DB and displaying it in list
	 */
	public void displayData(String request) {
		String[] data = { WeatherDb.KEY_ROWID, WeatherDb.KEY_WEEKDAY,
				WeatherDb.KEY_FAHRENHEIT, WeatherDb.KEY_SKYICON };
		Uri uri = Uri.parse(myContentProvider.CONTENT_URI + "/" + 1);
		Cursor cursor = getContentResolver().query(uri, data, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
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
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}