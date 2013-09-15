// Dan Annis
// JAVA 2 Week 2
// CONTENT PROVIDER
// **** NOT EASY!! **** //

package com.dinkydetails.weatherpull;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WeatherDb {
	//Setting up Global Contstants to be accessed later by the content provider
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PERIOD = "period";
	public static final String KEY_WEEKDAY = "weekday";
	public static final String KEY_FAHRENHEIT = "fahrenheit";
	public static final String KEY_SKYICON = "skyicon";
	//other constants
	private static final String LOG_TAG = "WeatherDb";
	public static final String SQLITE_TABLE = "Weather";

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_PERIOD + ","
			+ KEY_WEEKDAY + "," + KEY_FAHRENHEIT + "," + KEY_SKYICON + ");";

	private MyDataBaseHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	public WeatherDb(Context c) {
		super();
	}

	public void close() {
		sqLiteHelper.close();
	}

	public int deleteAll() {
		return sqLiteDatabase.delete(SQLITE_TABLE, null, null);
	}

	public Cursor getContact(int id) {
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

		Cursor cursor = db.query(SQLITE_TABLE, new String[] { KEY_PERIOD,
				KEY_WEEKDAY, KEY_FAHRENHEIT, KEY_SKYICON }, KEY_PERIOD + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}

	public static void onCreate(SQLiteDatabase db) {
		Log.w(LOG_TAG, DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
		onCreate(db);
	}

}
