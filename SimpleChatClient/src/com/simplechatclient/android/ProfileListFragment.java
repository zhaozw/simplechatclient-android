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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.core.Config;
import com.database.DatabaseProfile;

class ProfileListFragment extends Fragment implements View.OnClickListener {

	private Context context;
	private View view;
	private ArrayList<String> profiles_list;
	
	public static ProfileListFragment newInstance() {
		ProfileListFragment fragment = new ProfileListFragment();
		return fragment;
	}

	public ProfileListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.profile_list_fragment, container, false);
		context = container.getContext();		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		// profiles list
		Config current_config = new Config(context);
		List<DatabaseProfile> profiles = current_config.getProfiles();

		profiles_list = new ArrayList<String>();
		for (DatabaseProfile profile : profiles) {
			profiles_list.add(profile.getNick());
	    }

		ListView listview = (ListView)view.findViewById(R.id.listViewProfiles);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, profiles_list);
		listview.setAdapter(adapter);		
		listview.setOnItemClickListener(onProfileClick);
		
		Button profile_add_button = (Button)view.findViewById(R.id.profile_add_button);
		profile_add_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.profile_add_button:
				profile_add_button();
				break;
		}
	}

	private void profile_add_button()
	{
    	Intent addProfileIntent = new Intent(context, ProfileAddActivity.class);
    	addProfileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(addProfileIntent);
	}
	
	private OnItemClickListener onProfileClick = new OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	String nick = profiles_list.get(position).toString();
        	
        	Intent editProfileIntent = new Intent(context, ProfileEditActivity.class);
        	editProfileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	editProfileIntent.putExtra("nick", nick);
            startActivity(editProfileIntent);
        }
	};
	
	
}