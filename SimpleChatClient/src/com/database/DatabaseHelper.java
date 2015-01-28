/*
 * Simple Chat Client
 *
 *   Copyright (C) Piotr Łuczko <piotr.luczko@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.database;

import java.util.Random;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "config";

	public static final String TABLE_SETTINGS = "settings";
	public static final String TABLE_SETTINGS_ID = "id";
	public static final String TABLE_SETTINGS_CURRENT_PROFILE = "current_profile";
	public static final String TABLE_SETTINGS_UNIQUE_ID = "unique_id";
	
	public static final String TABLE_SETTINGS_ID_CURRENT = "current";
    
	public static final String TABLE_PROFILES = "profiles";
	public static final String TABLE_PROFILES_ID = "id";
	public static final String TABLE_PROFILES_NICK = "nick";
	public static final String TABLE_PROFILES_PASSWORD = "password";
	public static final String TABLE_PROFILES_FONT = "font";
	public static final String TABLE_PROFILES_BOLD = "bold";
	public static final String TABLE_PROFILES_ITALIC = "italic";
	public static final String TABLE_PROFILES_COLOR = "color";
    
	public static final String CREATE_TABLE_SETTINGS = "CREATE TABLE IF NOT EXISTS "+ TABLE_SETTINGS +" ("
			+ TABLE_SETTINGS_ID +" VARCHAR(7), "
    		+ TABLE_SETTINGS_CURRENT_PROFILE +" INTEGER, "
    		+ TABLE_SETTINGS_UNIQUE_ID +" VARCHAR(36)"
    		+ ")";
    
	public static final String CREATE_TABLE_PROFILES = "CREATE TABLE IF NOT EXISTS "+ TABLE_PROFILES +" ("
			+ TABLE_PROFILES_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
    		+ TABLE_PROFILES_NICK +" VARCHAR(32) UNIQUE, "
    		+ TABLE_PROFILES_PASSWORD +" TEXT, "
    		+ TABLE_PROFILES_FONT +" VARCHAR(250) DEFAULT \"verdana\", "
    		+ TABLE_PROFILES_BOLD +" VARCHAR(5) DEFAULT \"false\", "
    		+ TABLE_PROFILES_ITALIC +" VARCHAR(5) DEFAULT \"false\", "
    		+ TABLE_PROFILES_COLOR +" VARCHAR(6) DEFAULT \"000000\" "
    		+ ")";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_SETTINGS);
        db.execSQL(CREATE_TABLE_PROFILES);
        
        // init database
        createRandomUser(db);
    }
 
    public void createRandomUser(SQLiteDatabase db) {
    	
        Random mRandom = new Random();
    	int rand = mRandom.nextInt(1000);
    	
    	String current_profile = "~nick_tymczasowy"+String.valueOf(rand);
    	String uuid = UUID.randomUUID().toString();

    	ContentValues initialValuesProfile = new ContentValues();
    	initialValuesProfile.put(TABLE_PROFILES_NICK, current_profile);
    	initialValuesProfile.put(TABLE_PROFILES_PASSWORD, "");
    	long id = db.insert(TABLE_PROFILES, null, initialValuesProfile);

    	ContentValues initialValuesSettings = new ContentValues();
    	initialValuesSettings.put(TABLE_SETTINGS_ID, TABLE_SETTINGS_ID_CURRENT);
    	initialValuesSettings.put(TABLE_SETTINGS_CURRENT_PROFILE, id);
    	initialValuesSettings.put(TABLE_SETTINGS_UNIQUE_ID, uuid);
    	db.insert(TABLE_SETTINGS, null, initialValuesSettings);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
 
        // create new tables
        onCreate(db);
    }
}
