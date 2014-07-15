/*
 * Simple Chat Client
 *
 *   Copyright (C) 2014 Piotr Łuczko <piotr.luczko@gmail.com>
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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "config";

	private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_SETTINGS_ID = "id";
    private static final String TABLE_SETTINGS_CURRENT_PROFILE = "current_profile";
    private static final String TABLE_SETTINGS_UNIQUE_ID = "unique_id";
    
    private static final String TABLE_PROFILES = "profiles";
    private static final String TABLE_PROFILES_ID = "id";
    private static final String TABLE_PROFILES_NICK = "nick";
    private static final String TABLE_PROFILES_PASS = "pass";
    private static final String TABLE_PROFILES_FONT = "font";
    private static final String TABLE_PROFILES_BOLD = "bold";
    private static final String TABLE_PROFILES_ITALIC = "italic";
    private static final String TABLE_PROFILES_COLOR = "color";
    
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE "+ TABLE_SETTINGS +" ("
    		+ TABLE_SETTINGS_ID +" INTEGER PRIMARY KEY, "
    		+ TABLE_SETTINGS_CURRENT_PROFILE +" VARCHAR(32), "
    		+ TABLE_SETTINGS_UNIQUE_ID +" VARCHAR(36)"
    		+ ")";
    
    private static final String CREATE_TABLE_PROFILES = "CREATE TABLE "+ TABLE_PROFILES +" ("
    		+ TABLE_PROFILES_ID +" INTEGER PRIMARY KEY, "
    		+ TABLE_PROFILES_NICK +" VARCHAR(32), "
    		+ TABLE_PROFILES_PASS +" TEXT, "
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
