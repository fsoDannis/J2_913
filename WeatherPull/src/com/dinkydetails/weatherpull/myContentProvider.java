package com.dinkydetails.weatherpull;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class myContentProvider extends ContentProvider{
	//Setting up the Authority
	public static final String AUTHORITY= "com.dinkydetails.weatherpull.myContentProvider";
	
	
	public static class myData implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content//" + AUTHORITY + "/items");
		
		public static final String CONTENT_TYPE = "vnd.cursor.android.dir/vnd.dinkydetails.weatherpull.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.cursor.android.item/vnd.dinkydetails.weatherpull.item";
		  
		// Define Columns of provided Data - This is the stuff that is going to be stored from JSON
		//The definitions below will not be what I use for final... just following along...
		public static final String OBJECT_COLUMN = "object";
		public static final String YEAR_COLUMN = "year";
		public static final String PLACE_COLUMN = "place";
		
		// Defining the projections
		public static final String [] PROJECTION= {"_id",OBJECT_COLUMN,YEAR_COLUMN,PLACE_COLUMN};
		
		// Define constructor for the Data - preventing access to certain things 
		private  myData() {};
		
	}
	
	//Creating the Uri Matchers  - All items from the JSON
	public static final int ITEMS = 1;
	public static final int ITEMS_ID = 2;
	
	//Class to compare Uri to make sure the data is valid
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	//Setting up for the Uri
	static {
		uriMatcher.addURI(AUTHORITY, "items/", ITEMS);
		uriMatcher.addURI(AUTHORITY, "items/#", ITEMS_ID); //allow request of items by ID
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
		case ITEMS:
			return myData.CONTENT_TYPE;
		case ITEMS_ID:
			return myData.CONTENT_ITEM_TYPE;
		}
		return null;
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
		
		//Setting up cursor that will be returning to the projection created
		MatrixCursor result = new MatrixCursor(myData.PROJECTION);
		
		//Make sure there is data to return from my DataStorage Class             //ASSUME FILE_NAME is somewhere in DataStorage once fixed to Internal
		String JSONString = DataStorage.readStringFile(getContext(), myContentProvider.FILE_NAME);
		
		//Testing the UriMatcher
		switch(uriMatcher.match(uri)){
		case ITEMS:
			return myData.CONTENT_TYPE;
		case ITEMS_ID:
			return myData.CONTENT_ITEM_TYPE;
		}
		return null;
	}


	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
