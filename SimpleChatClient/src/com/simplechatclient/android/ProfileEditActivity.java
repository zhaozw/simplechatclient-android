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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.core.Config;
import com.database.DatabaseProfile;

public class ProfileEditActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_activity);
		
		Intent intent = getIntent();
		String nick = intent.getStringExtra("nick");

		Config current_config = new Config(this);
		DatabaseProfile profile = current_config.getProfile(nick);
		
		ToggleButton toggleButtonRegistered = (ToggleButton)findViewById(R.id.toggleButtonRegistered);
		EditText editTextNick = (EditText)findViewById(R.id.editTextNick);
		EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
		
		editTextNick.setText(profile.getNick());
		editTextPassword.setText(profile.getPassword());
		
		if (!nick.contains("~")) {
			toggleButtonRegistered.setChecked(true);
			editTextPassword.setVisibility(View.VISIBLE);
		} else {
			toggleButtonRegistered.setChecked(false);
			editTextPassword.setVisibility(View.INVISIBLE);
		}
	}
}
