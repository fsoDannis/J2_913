package com.dinkydetails.weatherpull;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
		Handler countdownHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
    
}
