package com.dinkydetails.weatherpull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;


public class myContentProvider extends ContentProvider{
	
	private MyDataBaseHelper dbHelper;
	
	//Creating the Uri Matchers  - All items from the JSON
	private static final int ALL_DAYS = 1;
	private static final int SINGLE_DAY = 2;
	
	//Setting up the Authority
	public static final String AUTHORITY= "com.dinkydetails.weatherpull.myContentProvider";
	

	public static class myData implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content//" + AUTHORITY + "/Weather");
		
		public static final String CONTENT_TYPE = "vnd.cursor.android.dir/vnd.dinkydetails.weatherpull.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.cursor.android.item/vnd.dinkydetails.weatherpull.item";
		  
		// Define Columns of provided Data - This is the stuff that is going to be stored from JSON
		//The definitions below will not be what I use for final... just following along in a video...
		public static final String OBJECT_COLUMN = "object";
		public static final String YEAR_COLUMN = "year";
		public static final String PLACE_COLUMN = "place";
		
		// Defining the projections
		public static final String [] PROJECTION= {"_id",OBJECT_COLUMN,YEAR_COLUMN,PLACE_COLUMN};
		
		// Define constructor for the Data - preventing access to certain things 
		private  myData() {};
		
	}
	
	
	//Class to compare Uri to make sure the data is valid
	//Setting up for the Uri
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "Weather/", ALL_DAYS);
		uriMatcher.addURI(AUTHORITY, "Weather/#", SINGLE_DAY); //allow request of items by ID
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		//Testing the UriMatcher
		switch(uriMatcher.match(uri)){
		case ALL_DAYS:
			return "com.dinkydetails.weatherpull.Weather";
		case SINGLE_DAY:
			return "com.dinkydetails.weatherpull.Weather";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(WeatherDb.SQLITE_TABLE);

		switch (uriMatcher.match(uri)) {
		case ALL_DAYS:
			// do nothing
			break;
		case SINGLE_DAY:
			String id = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(WeatherDb.KEY_ROWID + "=" + id);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		return cursor;

	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}