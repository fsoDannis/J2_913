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