package com.example.s54651719;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class dbcon {
	private SQLiteDatabase db = null;
	@SuppressLint("SdCardPath")
	public SQLiteDatabase onCreate(){
		try { 
			db = SQLiteDatabase.openDatabase( "/data/data/com.example.s54651719/emergencyDB",null,SQLiteDatabase.CREATE_IF_NECESSARY);
			db.execSQL("create table if not exists emergency(phoneNo text,title text,name text,surname text,message text,gps text)"); 
			db.execSQL("create table if not exists alert(fullDate text,day text,month text,year text)");
		}catch (SQLiteException e) {
			Log.d("SQLError",e.getMessage());
		}
		return db;
	}
}

