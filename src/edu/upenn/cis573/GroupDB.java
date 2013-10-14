package edu.upenn.cis573;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.widget.Toast;
import edu.upenn.cis573.R;
import android.annotation.SuppressLint;
import android.app.Activity;

public class GroupDB extends SQLiteOpenHelper{

	 // Database Version
    private static final int DATABASE_VERSION = 1;
    Cursor cursor;
    // Database Name
    private static final String DATABASE_NAME = "GContacts";
 
    // Contacts table name
    private static final String TABLE_CONTACT = "UserContacts";
 
    // Contacts Table Columns names
    private static final String KEY_NAME1 = "name1";
    private static final String KEY_NAME2 = "name2";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_EMAIL = "email";
    
    public GroupDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + KEY_NAME1 + " TEXT," + KEY_NAME2 + " TEXT,"
                + KEY_EMAIL + " TEXT," +KEY_NUMBER+" TEXT PRIMARY KEY"+")";
        db.execSQL(CREATE_GROUP_TABLE);
//        System.out.println("Created Table");
        //System.out.println("CONTACTS LIST"+retrieveContacts());
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
    	db.execSQL("DROP TABLE " + TABLE_CONTACT);
    }
    
   public void addC(GetDbValues g) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("REACHED HEREEE");
        ContentValues values = new ContentValues();
        values.put(KEY_NAME1, g.getname1()); 
        values.put(KEY_NAME2, g.getname2()); 
        values.put(KEY_EMAIL, g.getemail()); 
        values.put(KEY_NUMBER, g.getphone()); 
        // Inserting Row
        long newRowId;
        newRowId = db.insert(TABLE_CONTACT, null, values);
       // System.out.println("INSERTED");
      //  db.close(); // Closing database connection
    }
   
   public String retrieveContacts()
   {
	   try{
	   String contactlist = null;
	   SQLiteDatabase db = this.getReadableDatabase();
	   String query = "SELECT "+KEY_NUMBER+" FROM "+TABLE_CONTACT;
	   cursor = db.rawQuery(query,null);
	   cursor.moveToFirst();
	   if (cursor.moveToFirst()) {
           do {
		   contactlist+=cursor.getString(2);
           }while (cursor.moveToNext());
	   }
	   return contactlist;
	   }
	   catch(Exception e)
	   {
	   return " contacts exception occured "+e;
	   }
   }
 }
