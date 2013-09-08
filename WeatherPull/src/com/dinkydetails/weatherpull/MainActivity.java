package com.dinkydetails.weatherpull;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private EditText editTextZipCode = null;
	private Button buttonSearch = null;
	private TextView resultTextView = null;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        editTextZipCode = (EditText) findViewById(R.id.editTextZipCode);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
		if (editTextZipCode.getText().toString().length()==0){
			Toast.makeText(this, "Please Enter a Valid Zip Code!", Toast.LENGTH_LONG).show();
			return;
		}
		
		
		Handler myDataHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
			Log.i("SERVICE", "handlemessage() called");
			String response = null;	
			
			if (msg.arg1 == RESULT_OK && msg.obj !=null ){
				try{
					response = (String) msg.obj; 
					Log.i("JSON DATA", response);
					
					//search_string may be ZipCode
					displayData(search_string);
					
				}
				catch (Exception e){
					Log.e("JSON Response", e.toString());
				}
				}
			}
		};
		
		Messenger myDataMessenger = new Messenger(myDataHandler);
		
		Intent startDataServiceIntent = new Intent (getBaseContext(), WeatherService.class);
		startDataServiceIntent.putExtra("messenger", myDataMessenger);
		startDataServiceIntent.putExtra("user_input", search_string);
		startService(startDataServiceIntent);
		
		resultTextView.setText("Waiting....");
		
		}
	});
    
	if (savedInstanceState  != null){
		Log.i("SAVE INSTANCE", "saving instance");
		
		myList = (ArrayList<HashMap<String,String>>) savedInstanceState.getSerializable("saved");
		
		if (myList != null){
			SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.list_row);
				new String[] {"artist", "title", "place"}, new int[] {R.id.artist, R.id.title, R.id.place};
				
				listview.setAdapter(adapter)
		}
	}
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
