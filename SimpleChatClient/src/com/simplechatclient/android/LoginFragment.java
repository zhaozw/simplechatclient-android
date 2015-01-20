/*
 * Simple Chat Client
 *
 *   Copyright (C) 2015 Piotr Łuczko <piotr.luczko@gmail.com>
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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.core.Config;
import com.core.Network;
import com.core.Settings;
import com.database.DatabaseProfile;
import com.database.DatabaseSetting;

public class LoginFragment extends Fragment implements View.OnClickListener {

	private Context context;
	private View view;
	private ArrayList<String> profiles_list;
	
	public static LoginFragment newInstance() {
		LoginFragment fragment = new LoginFragment();
		return fragment;
	}

	public LoginFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_fragment, container, false);
		context = container.getContext();
		
		myStart();
		
		return view;
	}

	public void myStart() {
		
		Config current_config = new Config(context);
		List<DatabaseProfile> profiles = current_config.getProfiles();

		profiles_list = new ArrayList<String>();
		for (DatabaseProfile profile : profiles) {
			profiles_list.add(profile.getNick());
	    }

		Button login = (Button)view.findViewById(R.id.button_login);
		Spinner spinner = (Spinner)view.findViewById(R.id.spinnerNick);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, profiles_list);		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		String nick = Settings.getInstance().get("nick");
		int default_position = profiles_list.indexOf(nick);
		if (default_position != -1)
			spinner.setSelection(default_position); 
		
		spinner.setOnItemSelectedListener(spinnerListener);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_login)
			button_login();
	}

	private void button_login()
	{
		Intent intent = new Intent(context, TabsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


        // network start
        Network.getInstance().connect();

    }

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        	String selected_nick = profiles_list.get(position);
        	String nick = Settings.getInstance().get("nick");

        	if (selected_nick != nick)
        	{
        		Config current_config = new Config(context);
        		DatabaseProfile profile = current_config.getProfile(selected_nick);
        		DatabaseSetting setting = current_config.getSetting();
        		int selected_nick_id =  profile.getId();
        		
        		Settings.getInstance().set("nick", selected_nick);
        		Settings.getInstance().set("current_profile", Integer.toString(selected_nick_id));
        		
                Settings.getInstance().set("nick", profile.getNick());
                Settings.getInstance().set("password", profile.getPassword());
                Settings.getInstance().set("font", profile.getFont());
                Settings.getInstance().set("bold", profile.getBold());
                Settings.getInstance().set("italic", profile.getItalic());
                Settings.getInstance().set("color", profile.getColor());
        		
        		setting.setCurrent_profile(selected_nick_id);
        		current_config.updateSettings(setting);
        	}
        }

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	
}