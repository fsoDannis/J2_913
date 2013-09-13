//Dan Annis
//Sept. 2013

package com.dinkydetails.weatherpull;

import org.json.*;

import android.os.*;
import android.app.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	//Variable's for UI
	private EditText editTextZipCode = null;
	private Button buttonSearch = null;
	private TextView resultTextView = null;
	
	//Other Declarations
	static final String baseURL = "http://api.wunderground.com/api/a988d453ebe759ad/conditions/q/";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//setting up UI widgets
		resultTextView = (TextView) findViewById(R.id.resultTextView);
		editTextZipCode = (EditText) findViewById(R.id.editTextZipCode);
		
		//Look for any changes in the Edit Text and save the info to pass later
		editTextZipCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					searchWeather(editTextZipCode.getText().toString().trim());
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
	
	
	////////NEEED TO FIX THIS INSTANCE STATE THINGY		 -- MOVED UP to TOP
/*    
       if (savedInstanceState  != null){
       	Log.i("SAVE INSTANCE", "saving instance");

        	myList = (ArrayList<HashMap<String,String>>) savedInstanceState.getSerializable("saved");

        	if (myList != null){
        		SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.list_row);
        		new String[] {"artist", "title", "place"}, new int[] {R.id.artist, R.id.title, R.id.place};

       		listview.setAdapter(adapter)
        	}
        }
	}*/
	
		
		public void displayData(String Something) {

			String JSONString = DataStorage.readStringFile("my_data.txt");
			JSONObject response = null;
			String city = "N/A";
			String state = "N/A";
			String feelslike_string = "N/A";
			String temp_f = "N/A";
			String wind_string = "N/A";
			String wind_dir = "N/A";
			String visibility_mi = "N/A";
			String forcecast_url = "N/A";
		try{
			//Dig into the response object
			response = new JSONObject(JSONString);
			//grab the city
			city = response.getJSONObject("current_observation")
					.getJSONObject("display_location").getString("city");
			//grab the state
			state = response.getJSONObject("current_observation")
					.getJSONObject("display_location").getString("state");
			//grab the feels like weather
			feelslike_string = response.getJSONObject("current_observation")
					.getString("feelslike_string");
			//grab the temp
			temp_f = response.getJSONObject("current_observation").getString(
					"temp_f");
			//grab the wind
			wind_string = response.getJSONObject("current_observation")
					.getString("wind_string");
			//grab the wind dir
			wind_dir = response.getJSONObject("current_observation").getString(
					"wind_dir");
			//grab the wind visibility
			visibility_mi = response.getJSONObject("current_observation")
					.getString("visibility_mi");
			//grab the forcast URL
			forcecast_url = response.getJSONObject("current_observation")
					.getString("forecast_url");
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		// Take all the data and dump it into a textview
		StringBuffer message = new StringBuffer();
		message.append("city = ").append(city).append("\n");
		message.append("state = ").append(state).append("\n");
		message.append("feelslike_string = ").append(feelslike_string)
				.append("\n");
		message.append("temp_f = ").append(temp_f).append(" F\n");
		message.append("wind_string = ").append(wind_string).append("\n");
		message.append("wind_dir = ").append(wind_dir).append("\n");
		message.append("visibility_mi = ").append(visibility_mi).append("\n");
		message.append("forecast_url = ").append(forcecast_url).append("\n");

		//set the data 
		resultTextView.setText(message);

	}

}

