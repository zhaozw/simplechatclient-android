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

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.core.Config;
import com.core.Settings;
import com.database.DatabaseProfile;
import com.database.DatabaseSetting;

public class ProfileEditActivity extends ActionBarActivity implements View.OnClickListener {
	
	private DatabaseProfile profile;
	private Config current_config;
	private String nick;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_activity);
		context = getApplicationContext();
		
		nick = getIntent().getStringExtra("nick");

		current_config = new Config(this);
		profile = current_config.getProfile(nick);
		
		EditText editTextNick = (EditText)findViewById(R.id.editTextEditNick);
		EditText editTextPassword = (EditText)findViewById(R.id.editTextEditPassword);
		
		editTextNick.setText(profile.getNick());
		editTextPassword.setText(profile.getPassword());
		
		if (!nick.contains("~")) {
			editTextPassword.setVisibility(View.VISIBLE);
		} else {
			editTextPassword.setVisibility(View.INVISIBLE);
		}
		
		Button button_remove = (Button)findViewById(R.id.edit_profile_button_remove);
		Button button_save = (Button)findViewById(R.id.edit_profile_button_save);

		button_remove.setOnClickListener(this);
		button_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.edit_profile_button_remove:
				button_remove();
				break;
			case R.id.edit_profile_button_save:
				button_save();
				break;
		}
	}
	
	private void button_remove()
	{
		current_config.deleteProfile(nick);

		if (current_config.getProfilesCount() == 0)
		{
			current_config.createRandomUser();
		}

		if (Integer.valueOf(Settings.getInstance().get("current_profile")) == profile.getId())
		{
			List<DatabaseProfile> profiles = current_config.getProfiles();
			DatabaseProfile current_profile = profiles.get(0);
			
			DatabaseSetting current_setting = current_config.getSetting();
			current_setting.setCurrent_profile(current_profile.getId());

			Settings.getInstance().set("current_profile", String.valueOf(current_profile.getId()));
			
	        Settings.getInstance().set("nick", current_profile.getNick());
	        Settings.getInstance().set("password", current_profile.getPassword());
	        Settings.getInstance().set("font", current_profile.getFont());
	        Settings.getInstance().set("bold", current_profile.getBold());
	        Settings.getInstance().set("italic", current_profile.getItalic());
	        Settings.getInstance().set("color", current_profile.getColor());
		}
		
		Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.successfully_removed), Toast.LENGTH_SHORT);
		toast.show();

    	Intent profileListIntent = new Intent(context, MainActivity.class);
    	profileListIntent.putExtra("tab", "1"); // profile list
        startActivity(profileListIntent);
	}

	private void button_save()
	{
		EditText editTextNick = (EditText)findViewById(R.id.editTextEditNick);
		EditText editTextPassword = (EditText)findViewById(R.id.editTextEditPassword);
		
		if (editTextPassword.getVisibility() == View.VISIBLE)
		{
			String save_nick = editTextNick.getText().toString();
			String save_password = editTextPassword.getText().toString();
			
			if (save_nick.length() > 32)
				save_nick = save_nick.substring(0, 32);
			
			profile.setNick(save_nick);
			profile.setPassword(save_password);
		}
		else
		{
			String save_nick = editTextNick.getText().toString();
			save_nick = save_nick.replace("~","");
			
			if (save_nick.length() > 31)
				save_nick = save_nick.substring(0, 31);
			
			save_nick = "~"+save_nick;
			
			profile.setNick(save_nick);
		}
		
		current_config.updateProfile(profile);
		
		if (Integer.valueOf(Settings.getInstance().get("current_profile")) == profile.getId())
		{
			Settings.getInstance().set("nick", profile.getNick());
			Settings.getInstance().set("password", profile.getPassword());
		}
        
		Toast toast = Toast.makeText(context, getResources().getString(R.string.successfully_saved), Toast.LENGTH_SHORT);
		toast.show();

    	Intent profileListIntent = new Intent(context, MainActivity.class);
    	profileListIntent.putExtra("tab", "1"); // profile list
        startActivity(profileListIntent);
	}
}
