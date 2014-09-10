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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.core.Config;

public class ProfileAddFragment extends Fragment implements View.OnClickListener {

	private Context context;
	private View view;
	
	public static ProfileAddFragment newInstance() {
		ProfileAddFragment fragment = new ProfileAddFragment();
		return fragment;
	}

	public ProfileAddFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.profile_add_fragment, container, false);
		context = container.getContext();
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.profile_add, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case R.id.profile_add_button_add:
				add_new_profile();
				break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.add_profile_switch_registered:
				add_profile_switch_registered();
				break;
		}
	}

	private void add_profile_switch_registered()
	{
		CompoundButton compoundButtonRegistered = (CompoundButton)view.findViewById(R.id.add_profile_switch_registered);
		EditText editTextNewPassword = (EditText)view.findViewById(R.id.editTextNewPassword);
		
		if (compoundButtonRegistered.isChecked())
		{
			editTextNewPassword.setVisibility(View.VISIBLE);
		}
		else
		{
			editTextNewPassword.setVisibility(View.GONE);
		}
	}
	
	private void add_new_profile()
	{
		EditText editTextNewNick = (EditText)view.findViewById(R.id.editTextNewNick);
		EditText editTextNewPassword = (EditText)view.findViewById(R.id.editTextNewPassword);
		
		String add_nick = null;
		String add_password = null;
		
		if (editTextNewPassword.getVisibility() == View.VISIBLE)
		{
			add_nick = editTextNewNick.getText().toString();
			add_password = editTextNewPassword.getText().toString();
			
			if (add_nick.length() > 32)
				add_nick = add_nick.substring(0, 32);			
		}
		else
		{
			add_nick = editTextNewNick.getText().toString();
			add_nick = add_nick.replace("~","");
			
			if (add_nick.length() > 31)
				add_nick = add_nick.substring(0, 31);
			
			add_nick = "~"+add_nick;
		}

		if (add_nick.length() <= 1)
		{
			Toast toast = Toast.makeText(context, getResources().getString(R.string.profile_length_too_small), Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		
		Config current_config = new Config(context);
		if (!current_config.profileExists(add_nick))
		{
			current_config.addProfile(add_nick, add_password);
			
			Toast toast = Toast.makeText(context, getResources().getString(R.string.successfully_added), Toast.LENGTH_SHORT);
			toast.show();
	
	    	Intent profileListIntent = new Intent(context, ProfileActivity.class);
	    	profileListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	profileListIntent.putExtra("tab", "1"); // profile list
	        startActivity(profileListIntent);
		} else {
			Toast toast = Toast.makeText(context, getResources().getString(R.string.profile_already_exists), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		CompoundButton compoundButtonRegistered = (CompoundButton)view.findViewById(R.id.add_profile_switch_registered);
		compoundButtonRegistered.setOnClickListener(this);
		
		add_profile_switch_registered();
	}
}
