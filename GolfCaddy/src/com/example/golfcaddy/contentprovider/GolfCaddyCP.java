package com.example.golfcaddy.contentprovider;

import com.example.golfcaddy.database.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GolfCaddyCP extends ContentProvider {
	SQLiteDatabase db;
	
	public static final Uri CONTENT_URI = Uri.parse("content://golfcaddy/scores/"); 
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = db.insert(DBHelper.TABLE_NAME, null, values);
		return ContentUris.withAppendedId(CONTENT_URI, id);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return db.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
	}

}
