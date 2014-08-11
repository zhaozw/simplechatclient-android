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

package com.simplechatclient.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.core.Config;
import com.core.Network;
import com.core.Settings;
import com.database.DatabaseProfile;
import com.database.DatabaseSetting;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Config current_config = new Config(getApplicationContext());
        
        DatabaseSetting current_settings = current_config.getSetting();
        DatabaseProfile current_profile = current_config.getProfile(current_settings.getCurrent_profile());
        
        Settings.getInstance().set("current_nick", current_settings.getCurrent_profile());
        Settings.getInstance().set("unique_id", current_settings.getUnique_id());
        
        Settings.getInstance().set("nick", current_profile.getNick());
        Settings.getInstance().set("password", current_profile.getPassword());
        Settings.getInstance().set("font", current_profile.getFont());
        Settings.getInstance().set("bold", current_profile.getBold());
        Settings.getInstance().set("italic", current_profile.getItalic());
        Settings.getInstance().set("color", current_profile.getColor());
        
        setContentView(R.layout.welcome);
        
        TextView nickText = (TextView)findViewById(R.id.textNickView);
        nickText.setText(current_profile.getNick());
    }

    public void button_profiles(View v)
    {
    	Intent intent = new Intent(this, ProfilesManager.class);
        startActivity(intent);    	
    }
    
    public void button_connect(View v)
    {
        Network.getInstance().connect();

        //Network.getInstance().send("JOIN #scc");
    }
    
    public void button_disconnect()
    {
    	Network.getInstance().disconnect();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    */
}
