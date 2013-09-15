package com.dinkydetails.weatherpull;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Weather";
	private static final int DATABASE_VERSION = 1;

	MyDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		WeatherDb.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		WeatherDb.onUpgrade(db, oldVersion, newVersion);
	}

}