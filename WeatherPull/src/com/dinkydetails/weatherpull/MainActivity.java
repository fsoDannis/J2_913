package com.dinkydetails.weatherpull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//Variable's for UI
	private EditText editTextZipCode = null;
	private Button buttonSearch = null;
	private TextView resultTextView = null;
	
	//Other Declarations
	static final String baseURL = "http://api.wunderground.com/api/a988d453ebe759ad/conditions/q/";

	//JSONObject results
	HashMap<String, String> _history;


	// Used to receive messages back from the service.
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				String string = bundle.getString(WeatherService.FILEPATH);
				int resultCode = bundle.getInt(WeatherService.RESULT);

				// Inform the user that all is well.
				if (resultCode == RESULT_OK) {
					Toast.makeText(MainActivity.this,
							"Download complete. Download URI: " + string,
							Toast.LENGTH_LONG).show();
					editTextZipCode.setText("Download Complete");

					// Call displayData() to populate the Text Views
					displayData();
				} else {
					editTextZipCode.setText("Not Connected to Internet");
					Toast.makeText(MainActivity.this,
							"Attempting to use saved data.",
							Toast.LENGTH_LONG).show();
					displayData();
					Toast.makeText(MainActivity.this,
							"Local data used.",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_context = this;

		//View Set Up
		setContentView(R.layout.activity_main);

		//Setting up the Individual Views
		editTextZipCode = (EditText) findViewById(R.id.editTextZipCode);
		//buttonSearch = (Button) findViewById(R.id.buttonSearch);


		//Place where things are going to be displayed
//		tv_city = (TextView) findViewById(R.id.tv_city);
//		tv_state = (TextView) findViewById(R.id.tv_state);
//		tv_feelsLike = (TextView) findViewById(R.id.tv_feelsLike);
//		tv_actualTemp = (TextView) findViewById(R.id.tv_actualTemp);

		
	}


	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(WeatherService.NOTIFICATION));
	}


	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	
	
	public void onClick(View view) {

		// Detect network connection
		_connected = WebConnection.getConnectionStatus(_context);
		if(_connected == true){
			Log.i("Connection", WebConnection.getConnectionType(_context));

			Intent intent = new Intent(this, WeatherService.class);
			EditText field = (EditText) findViewById(R.id.editTextZipCode);
			String zipCode = field.getText().toString().toUpperCase(Locale.US);
			// Populate values that will be supplied to the service


			intent.putExtra(WeatherService.FILENAME, "JSONData.txt");
			intent.putExtra(WeatherService.URL_STRING,baseURL + zipCode + ".json");
			startService(intent);
			//editTextZipCode.setText("Downloading the Data");
		} else {
			editTextZipCode.setText("Whoops Not Connected");
			Toast.makeText(MainActivity.this,
					"Saving Data",
					Toast.LENGTH_LONG).show();
		}
	}


	public void displayData(){

		ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
		int numberOfObjects = 0;
		@SuppressWarnings("unused")
		int length = -1;

		try {
			FileInputStream fis = openFileInput("JSONData.txt");
			StringBuffer fileContent = new StringBuffer("");
			byte[] buffer = new byte[1024];

			try {

				while ((length = fis.read(buffer)) != -1) {
					fileContent.append(new String(buffer));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			String JSONString = new String(fileContent);
			JSONArray inputArray = null;
			try {
				inputArray = new JSONArray(JSONString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			numberOfObjects = inputArray.length();
			Log.i("numberOfObjects", "The number of objects is: " + numberOfObjects);

			for (int i = 0; i < numberOfObjects; i++)
			{
				try {	
					String url = String.format(baseURL + editTextZipCode + ".json");
					String response = url;

					JSONObject message = null;
					//Weather result = null;


					JSONObject jo = inputArray.getJSONObject(i);
					message = (new JSONObject(response)).getJSONObject("current_observation");
					//	result = new Weather();
					String city = message.getJSONObject("display_location").getString("city");
					String state = message.getJSONObject("display_location").getString("state");
					String feelsLike = message.getString("feelslike_string");
					String actualTemp =message.getString("temp_f");

					HashMap<String, String>displayMap = new HashMap<String, String>();
					displayMap.put("city", city);
					displayMap.put("state", state);
					displayMap.put("feelsLike", feelsLike);
					displayMap.put("actualTemp", actualTemp);

					myList.add(displayMap);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if(numberOfObjects == 0)
			{
				editTextZipCode.setText("No results for entered zip");

			}
			SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.list_row,
					new String[] { "city", "state", "feelsLike", "actualTemp"}, new int[] {R.id.city, R.id.state, R.id.feelsLike, R.id.actualTemp});

					listview.setAdapter(adapter);
			
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
					
					
					
					
	
	
  //  @Override
  //  protected void onCreate(Bundle savedInstanceState) {
  //      super.onCreate(savedInstanceState);

    
        	
//	@Override
	//public void onClick(View v) {
		
	/*	//Grab the text that is entered.
		String zipText = editTextZipCode.getText().toString();
		
		//Hide the Keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(buttonSearch.getWindowToken(), 0);

		//Detect Network Connection
		_connected = WebConnection.getConnectionStatus(_context); 
		if(_connected){
		Log.i("NETWORK CONNECTION ", WebConnection.getConnectionType(_context)); 
		}
		
		//CallBack Method
		Handler myDataHandler = new Handler(){

			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				updateInfo();
			}
		};
		
		
		String tempURL = "";
		tempURL = new String(baseURL + zipText + ".json");
			
		URL finalURL;
		
		try{
			finalURL = new URL(tempURL);
			//Log.i("FINAL URL", finalURL.toString());
			
			Messenger myMessenger = new Messenger(myDataHandler);
            Intent myIntent = new Intent(_context, WeatherService.class);
            myIntent.putExtra("messenger", myMessenger);
            myIntent.putExtra("zipCode", zipText);
            myIntent.putExtra("finalURL", finalURL.toString());
            
            //Starting Service
            startService(myIntent);
		
		} catch (MalformedURLException e){
			Log.e("BAD URL", "MALFORMED URL");
			tv_city.setText("Your CITY must be COLD... Temp UNKNOWN!");
			tv_state.setText("Your STATE must be COLD... Temp UNKNOWN!");
			tv_feelsLike.setText("You must be COLD... Temp UNKNOWN!");
			tv_actualTemp.setText("You must be COLD... Temp UNKNOWN!");
			editTextZipCode.setText(tempURL);
		} finally {
			// This is done even if try block fails
			Log.i("LOG", "It Ran...");
		}
	}
        });

	////////NEEED TO FIX THIS INSTANCE STATE THINGY		
    
//        if (savedInstanceState  != null){
//        	Log.i("SAVE INSTANCE", "saving instance");
//
//        	myList = (ArrayList<HashMap<String,String>>) savedInstanceState.getSerializable("saved");
//
//        	if (myList != null){
//        		SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.list_row);
//        		new String[] {"artist", "title", "place"}, new int[] {R.id.artist, R.id.title, R.id.place};
//
//        		listview.setAdapter(adapter)
//        	}
//        }
	}
	
	public void displayData(String Something){
		
		if(this.myList != null){
			this.myList.clear();
		}
		String JSONString = DataStorage.readStringFile(getBaseContext(), "my_data.txt");
		
		JSONObject job = null;
		JSONArray recordArray = null;
		JSONObject field = null;
		
		String title;
		String place;
		
		try{
			job = new JSONObject(JSONString);
			recordArray = job.getJSONArray("records");
			
			for(int i = 0; i < 10 ; i++){
				field = recordArray.getJSONObject(i).getJSONObject("fields");
				//Get the items from the Weather API
				
				HashMap<String, String> displayMap = new HashMap<String, String>();
				//PUT the items into the hashmap
				this.myList.add(displayMap);
				
				Log.i("Display Data, JSON record Array"+ i, "artist="+ artist + ", title="+ title+", place="+ place);
				
				SimpleAdapter adapter = new SimpleAdapter(this, this.myList, R.layout.list_row, 
						new String[] {"artist", "title","place"}, new int[]{R.id.artist, R.id.title, R.id.place});
				
				listview.setAdapter(adapter);
				}
			catch (JSONException e){
				Log.e("JSON Exception", e.toString());
				
			}
		
		}
		@Override
		
}
	
	public void updateInfo() {
		// TODO Auto-generated method stub
		
	}

	

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }*/