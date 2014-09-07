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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.core.Messages;
import com.core.Network;
import com.core.Settings;
import com.models.Channels;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabsFragment extends Fragment {

	private static final String TAG = "TabsFragment";
	
	private Context context;
	private View view;
	
	private ArrayList<String> listItems;
	private ArrayAdapter<String> adapter;
	ListView listview;
	EditText editText;
	private String name;
	
	public static TabsFragment newInstance(String name) {
		TabsFragment fragment = new TabsFragment(name);
		return fragment;
	}
	
	public TabsFragment(String name) {
		listItems = new ArrayList<String>();
		this.name = name;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		listItems = null;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.action_logout:
				Network.getInstance().disconnect();
				
				Intent profileListIntent = new Intent(context, MainActivity.class);
	        	profileListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	profileListIntent.putExtra("tab", "0"); // profile list
	            startActivity(profileListIntent);
	            
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tabs_fragment, container, false);
		context = container.getContext();		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		listview = (ListView)view.findViewById(R.id.listViewChannel);
		editText = (EditText)view.findViewById(R.id.editTextChannel);

		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);
		listview.setAdapter(adapter);
		
		editText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
		            
		        	String data = editText.getText().toString();
		        	Log.i(TAG, "sending: "+data);
		        	if (!name.equalsIgnoreCase(Channels.STATUS))
		        	{
		        		String networkData = String.format("PRIVMSG %s :%s", name, data);
		        		Network.getInstance().send(networkData);
		        		
		        		String display = String.format("<%s> %s", Settings.getInstance().get("nick"), data);
		        		Messages.getInstance().showMessage(name, display);
		        	}
		        	else
		        	{
		        		Network.getInstance().send(data);
		        	}

		        	// clear
		        	editText.setText("");
		        	
		            handled = true;
		        }
		        return handled;
		    }
		});
	}
		
	public void addMessage(String data)
	{
		if (adapter == null) return;
		
		listItems.add(data);
		adapter.notifyDataSetChanged();
		
		scrollToBottom();
	}
	
	private void scrollToBottom() {
		listview.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	        	listview.setSelection(adapter.getCount() - 1);
	        }
	    });
	}

}
