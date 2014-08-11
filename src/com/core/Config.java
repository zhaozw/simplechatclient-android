package com.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.database.DatabaseHelper;
import com.database.DatabaseProfile;
import com.database.DatabaseSetting;


public class Config {
	
	private static final String TAG = "Config";
    private DatabaseHelper myDatabaseHelper;
    
	public Config(Context context)
	{
		myDatabaseHelper = new DatabaseHelper(context);
	}

	public DatabaseSetting getSetting()
	{
		DatabaseSetting setting = new DatabaseSetting();
		
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		try
		{
			String[] columns = {DatabaseHelper.TABLE_SETTINGS_CURRENT_PROFILE, DatabaseHelper.TABLE_SETTINGS_UNIQUE_ID};

			Cursor cursor =
		            db.query(DatabaseHelper.TABLE_SETTINGS, // table
            		columns, // column names
            		DatabaseHelper.TABLE_SETTINGS_ID +" = ?", // selections
		            new String[] { String.valueOf(DatabaseHelper.TABLE_SETTINGS_ID_CURRENT) }, // selections args
		            null, // group by
		            null, // having
		            null, // order by
		            null); // limit
		
			if (cursor != null)
				cursor.moveToFirst();
			
			String current_profile = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_SETTINGS_CURRENT_PROFILE));
			String unique_id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_SETTINGS_UNIQUE_ID));
			
			setting.setCurrent_profile(current_profile);
			setting.setUnique_id(unique_id);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (db != null)
				db.close();
		}
		
		return setting;		
	}
	
	public void updateSettings(String current_profile, String unique_id)
	{
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		try
		{
			ContentValues setting = new ContentValues();
			setting.put(DatabaseHelper.TABLE_SETTINGS_CURRENT_PROFILE, current_profile);
			setting.put(DatabaseHelper.TABLE_SETTINGS_UNIQUE_ID, unique_id);		
			
			db.update(DatabaseHelper.TABLE_SETTINGS, setting, DatabaseHelper.TABLE_SETTINGS_ID+" = ?", new String[] { String.valueOf(DatabaseHelper.TABLE_SETTINGS_ID_CURRENT) });

		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (db != null)
				db.close();
		}
	}
	
	public DatabaseProfile getProfile(String get_nick)
	{
		DatabaseProfile profile = new DatabaseProfile();
		
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		try
		{
			String[] columns = {DatabaseHelper.TABLE_PROFILES_NICK, DatabaseHelper.TABLE_PROFILES_PASSWORD, DatabaseHelper.TABLE_PROFILES_FONT, DatabaseHelper.TABLE_PROFILES_BOLD, DatabaseHelper.TABLE_PROFILES_ITALIC, DatabaseHelper.TABLE_PROFILES_COLOR};

			Cursor cursor =
		            db.query(DatabaseHelper.TABLE_PROFILES, // table
            		columns, // column names
            		DatabaseHelper.TABLE_PROFILES_NICK+" = ?", // selections
		            new String[] { String.valueOf(get_nick) }, // selections args
		            null, // group by
		            null, // having
		            null, // order by
		            null); // limit
		
			if (cursor != null)
				cursor.moveToFirst();
			
			String nick = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_NICK));
			String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_PASSWORD));
			String font = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_FONT));
			String bold = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_BOLD));
			String italic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ITALIC));
			String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_COLOR));
			
			profile.setNick(nick);
			profile.setPassword(password);
			profile.setFont(font);
			profile.setBold(bold);
			profile.setItalic(italic);
			profile.setColor(color);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (db != null)
				db.close();
		}
		
		return profile;
	}
	
	public List<DatabaseProfile> getProfiles()
	{
		List<DatabaseProfile> profiles = new ArrayList<DatabaseProfile>();

		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();

		try
		{
			String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PROFILES;
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			if (cursor.moveToFirst()) {
		        do {
					String nick = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_NICK));
					String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_PASSWORD));
					String font = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_FONT));
					String bold = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_BOLD));
					String italic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ITALIC));
					String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_COLOR));
					
		        	DatabaseProfile profile = new DatabaseProfile();
					profile.setNick(nick);
					profile.setPassword(password);
					profile.setFont(font);
					profile.setBold(bold);
					profile.setItalic(italic);
					profile.setColor(color);
		 
		            profiles.add(profile);
		        } while (cursor.moveToNext());
		    }
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (db != null)
				db.close();
		}

		return profiles;
	}
	
	public void addProfile(String nick, String password, String font, String bold, String italic, String color)
	{
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		
		ContentValues new_profile = new ContentValues();

		new_profile.put(DatabaseHelper.TABLE_PROFILES_NICK, nick);
		new_profile.put(DatabaseHelper.TABLE_PROFILES_PASSWORD, password);
		new_profile.put(DatabaseHelper.TABLE_PROFILES_FONT, font);
		new_profile.put(DatabaseHelper.TABLE_PROFILES_BOLD, bold);
		new_profile.put(DatabaseHelper.TABLE_PROFILES_ITALIC, italic);
		new_profile.put(DatabaseHelper.TABLE_PROFILES_COLOR, color);

		db.insert(DatabaseHelper.TABLE_PROFILES, null, new_profile);
		
		db.close();
	}
	
	public void updateProfile(String nick, String password, String font, String bold, String italic, String color)
	{
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		try
		{
			ContentValues profile = new ContentValues();
			profile.put(DatabaseHelper.TABLE_PROFILES_NICK, nick);
			profile.put(DatabaseHelper.TABLE_PROFILES_PASSWORD, password);		
			profile.put(DatabaseHelper.TABLE_PROFILES_FONT, font);		
			profile.put(DatabaseHelper.TABLE_PROFILES_BOLD, bold);		
			profile.put(DatabaseHelper.TABLE_PROFILES_ITALIC, italic);		
			profile.put(DatabaseHelper.TABLE_PROFILES_COLOR, color);		
			
			db.update(DatabaseHelper.TABLE_PROFILES, profile, DatabaseHelper.TABLE_PROFILES_NICK+" = ?", new String[] { String.valueOf(nick) });

		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (db != null)
				db.close();
		}
	}
	
	public void deleteProfile(String nick)
	{
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		try
		{
			db.delete(DatabaseHelper.TABLE_PROFILES, DatabaseHelper.TABLE_PROFILES_NICK + " = ?", new String[] { String.valueOf(nick) });
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (db != null)
				db.close();
		}
	}
}
