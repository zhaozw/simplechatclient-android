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
        Cursor cursor = null;

        try
        {
            String[] columns = {DatabaseHelper.TABLE_SETTINGS_CURRENT_PROFILE, DatabaseHelper.TABLE_SETTINGS_UNIQUE_ID};

            cursor =
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

            int current_profile = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_SETTINGS_CURRENT_PROFILE));
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
            if (cursor != null)
                cursor.close();

            if (db != null)
                db.close();
        }

        return setting;
    }

    public DatabaseProfile getProfile(int nick_id)
    {
        DatabaseProfile profile = new DatabaseProfile();

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;

        try
        {
            String[] columns = {DatabaseHelper.TABLE_PROFILES_ID, DatabaseHelper.TABLE_PROFILES_NICK, DatabaseHelper.TABLE_PROFILES_PASSWORD, DatabaseHelper.TABLE_PROFILES_FONT, DatabaseHelper.TABLE_PROFILES_BOLD, DatabaseHelper.TABLE_PROFILES_ITALIC, DatabaseHelper.TABLE_PROFILES_COLOR};

            cursor =
                    db.query(DatabaseHelper.TABLE_PROFILES, // table
                    columns, // column names
                    DatabaseHelper.TABLE_PROFILES_ID+" = ?", // selections
                    new String[] { String.valueOf(nick_id) }, // selections args
                    null, // group by
                    null, // having
                    null, // order by
                    null); // limit

            if (cursor != null)
                cursor.moveToFirst();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ID));
            String nick = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_NICK));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_PASSWORD));
            String font = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_FONT));
            String bold = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_BOLD));
            String italic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ITALIC));
            String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_COLOR));

            profile.setId(id);
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
            if (cursor != null)
                cursor.close();

            if (db != null)
                db.close();
        }

        return profile;
    }

    public DatabaseProfile getProfile(String profile_nick)
    {
        DatabaseProfile profile = new DatabaseProfile();

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;

        try
        {
            String[] columns = {DatabaseHelper.TABLE_PROFILES_ID, DatabaseHelper.TABLE_PROFILES_NICK, DatabaseHelper.TABLE_PROFILES_PASSWORD, DatabaseHelper.TABLE_PROFILES_FONT, DatabaseHelper.TABLE_PROFILES_BOLD, DatabaseHelper.TABLE_PROFILES_ITALIC, DatabaseHelper.TABLE_PROFILES_COLOR};

            cursor =
                    db.query(DatabaseHelper.TABLE_PROFILES, // table
                    columns, // column names
                    DatabaseHelper.TABLE_PROFILES_NICK+" = ?", // selections
                    new String[] { String.valueOf(profile_nick) }, // selections args
                    null, // group by
                    null, // having
                    null, // order by
                    null); // limit

            if (cursor != null)
                cursor.moveToFirst();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ID));
            String nick = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_NICK));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_PASSWORD));
            String font = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_FONT));
            String bold = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_BOLD));
            String italic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ITALIC));
            String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_COLOR));

            profile.setId(id);
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
            if (cursor != null)
                cursor.close();

            if (db != null)
                db.close();
        }

        return profile;
    }

    public long getProfilesCount()
    {
        long count = 0;

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;

        try
        {
            String selectQuery = "SELECT COUNT(*) AS c FROM " + DatabaseHelper.TABLE_PROFILES;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null)
                cursor.moveToFirst();

            count = cursor.getInt(cursor.getColumnIndexOrThrow("c"));
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

        return count;
    }

    public boolean profileExists(String nick)
    {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;

        try
        {
            String selectQuery = "SELECT COUNT(*) AS c FROM " + DatabaseHelper.TABLE_PROFILES + " WHERE nick = \""+ nick +"\"";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null)
                cursor.moveToFirst();

            long count = cursor.getInt(cursor.getColumnIndexOrThrow("c"));
            if (count > 0)
                return true;
            else
                return false;
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

        return false;
    }

    public List<DatabaseProfile> getProfiles()
    {
        List<DatabaseProfile> profiles = new ArrayList<>();

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;

        try
        {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PROFILES;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ID));
                    String nick = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_NICK));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_PASSWORD));
                    String font = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_FONT));
                    String bold = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_BOLD));
                    String italic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_ITALIC));
                    String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TABLE_PROFILES_COLOR));

                    DatabaseProfile profile = new DatabaseProfile();
                    profile.setId(id);
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
            if (cursor != null)
                cursor.close();

            if (db != null)
                db.close();
        }

        return profiles;
    }

    public void createRandomUser()
    {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        try
        {
            myDatabaseHelper.createRandomUser(db);
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

    public void updateSettings(DatabaseSetting setting)
    {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        try
        {
            ContentValues update_setting = new ContentValues();
            update_setting.put(DatabaseHelper.TABLE_SETTINGS_CURRENT_PROFILE, setting.getCurrent_profile());
            update_setting.put(DatabaseHelper.TABLE_SETTINGS_UNIQUE_ID, setting.getUnique_id());

            db.update(DatabaseHelper.TABLE_SETTINGS, update_setting, DatabaseHelper.TABLE_SETTINGS_ID+" = ?", new String[] { String.valueOf(DatabaseHelper.TABLE_SETTINGS_ID_CURRENT) });
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

    public long addProfile(String nick, String password)
    {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();

        ContentValues new_profile = new ContentValues();

        new_profile.put(DatabaseHelper.TABLE_PROFILES_NICK, nick);
        new_profile.put(DatabaseHelper.TABLE_PROFILES_PASSWORD, password);
        //new_profile.put(DatabaseHelper.TABLE_PROFILES_FONT, font);
        //new_profile.put(DatabaseHelper.TABLE_PROFILES_BOLD, bold);
        //new_profile.put(DatabaseHelper.TABLE_PROFILES_ITALIC, italic);
        //new_profile.put(DatabaseHelper.TABLE_PROFILES_COLOR, color);

        long id = db.insert(DatabaseHelper.TABLE_PROFILES, null, new_profile);

        db.close();

        return id;
    }

    public void updateProfile(DatabaseProfile profile)
    {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        try
        {
            ContentValues update_profile = new ContentValues();
            update_profile.put(DatabaseHelper.TABLE_PROFILES_NICK, profile.getNick());
            update_profile.put(DatabaseHelper.TABLE_PROFILES_PASSWORD, profile.getPassword());
            update_profile.put(DatabaseHelper.TABLE_PROFILES_FONT, profile.getFont());
            update_profile.put(DatabaseHelper.TABLE_PROFILES_BOLD, profile.getBold());
            update_profile.put(DatabaseHelper.TABLE_PROFILES_ITALIC, profile.getItalic());
            update_profile.put(DatabaseHelper.TABLE_PROFILES_COLOR, profile.getColor());

            db.update(DatabaseHelper.TABLE_PROFILES, update_profile, DatabaseHelper.TABLE_PROFILES_ID+" = ?", new String[] { String.valueOf(profile.getId()) });
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
