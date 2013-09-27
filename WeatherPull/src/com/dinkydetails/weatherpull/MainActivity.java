// Dan Annis
// JAVA 2 Week 2
// CONTENT PROVIDER
// **** NOT EASY!! **** //

package com.dinkydetails.weatherpull;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ExpandableListView;


@SuppressLint({ "HandlerLeak", "HandlerLeak" })
public class MainActivity extends FragmentActivity implements
		View.OnClickListener {

	/**
	 * Declaring variables to be used in the class
	 */
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	public static int nClickedBtn = 0;
	String zipCode = null;
	public static String ZipCode;

	static final String baseURL = "api.wunderground.com/api/a988d453ebe759ad/forecast10day/q/";

	/**
	 * This is the first function of class that gets launched when a activity is
	 * started
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration config = getResources().getConfiguration();

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		/**
		 * Check the device orientation and act accordingly
		 */
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			/**
			 * Landscape mode of the device
			 */
			LM_Fragment ls_fragment = new LM_Fragment();
			ls_fragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, ls_fragment).commit();
		} else {
			/**
			 * Portrait mode of the device
			 */
			PM_Fragment pm_fragment = new PM_Fragment();

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, pm_fragment).commit();
		}
		fragmentTransaction.commit();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {

	}
}
