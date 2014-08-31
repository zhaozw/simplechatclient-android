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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabsFragment extends Fragment {

	private Context context;
	private View view;
	
	private ArrayList<String> listItems;
	private ArrayAdapter<String> adapter;
	
	public static TabsFragment newInstance() {
		TabsFragment fragment = new TabsFragment();
		return fragment;
	}
	
	public TabsFragment() {
		listItems = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tabs_fragment, container, false);
		context = container.getContext();		

		ListView listview = (ListView)view.findViewById(R.id.listView1);

		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);
		listview.setAdapter(adapter);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		
	}
	
	public void addMessage(String data)
	{
		if (adapter == null) return;
		
		listItems.add(data);
		adapter.notifyDataSetChanged();
	}

}
