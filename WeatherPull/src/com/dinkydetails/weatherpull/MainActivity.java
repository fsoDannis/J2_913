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

	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }