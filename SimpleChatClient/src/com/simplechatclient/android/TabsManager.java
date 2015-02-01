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

package com.simplechatclient.android;

import java.util.HashMap;
import java.util.Map.Entry;

import android.util.Log;

import com.models.Channels;

public class TabsManager {
    private static TabsManager instance = new TabsManager();
    public static synchronized TabsManager getInstance() {return instance; }

    private static final String TAG = "TabsManager";
    private HashMap<String, TabsChannel> tabs;
    private TabsActivity tabsActivity;

    public TabsManager()
    {
    	tabs = new HashMap<String, TabsChannel>();
    }
    
	public TabsActivity getTabsActivity() {
		return tabsActivity;
	}

	public void setTabsActivity(TabsActivity tabsActivity) {
		this.tabsActivity = tabsActivity;
	}

	public void add(String channel)
    {
		Log.i(TAG, "add: "+channel);
		
    	TabsChannel NewTabChannel = new TabsChannel();
    	NewTabChannel.setAvatar(null);
    	NewTabChannel.setDisplayedOptions(false);
    	NewTabChannel.setName(channel);
    	NewTabChannel.setFragment(TabsFragment.newInstance().setName(channel));
    	NewTabChannel.setOffline(false);
    	NewTabChannel.setPosition(tabs.size());
    	
    	tabs.put(channel, NewTabChannel);
    	
    	if (channel != Channels.STATUS)
    		this.tabsActivity.add(channel);

    	Log.i(TAG, "added: "+channel +" size: "+tabs.size());
    }

    public void remove(String channel)
    {
        Log.i(TAG, "remove "+channel);

    	for(Entry<String, TabsChannel> entry : tabs.entrySet()) {
    	    TabsChannel tabsChannel = entry.getValue();
    	    if (tabsChannel.getName() == channel)
    	    {
                Log.i(TAG, "removed "+channel);
    	    	tabsChannel.setFragment(null);
    	    	tabsChannel = null;
    	    	return;
    	    }
    	}
    }
    
    public void removeAll()
    {
        Log.i(TAG, "remove all");

    	for(Entry<String, TabsChannel> entry : tabs.entrySet()) {
    	    TabsChannel tabsChannel = entry.getValue();
    	    tabsChannel.setFragment(null);
    	    tabsChannel = null;
    	}
    	
    	tabs.clear();
    }
    
    public int count()
    {
    	return tabs.size();
    }

    public String getName(int position)
    {
    	for(Entry<String, TabsChannel> entry : tabs.entrySet()) {
    	    TabsChannel tabsChannel = entry.getValue();
    		if (tabsChannel.getPosition() == position)
    			return tabsChannel.getName();
    	}
    	return null;
    }
    
    public TabsFragment get(int position)
    {
        /*
        if (tabs.size() == 0)
        {
            this.add(Channels.STATUS);
        }
        */

    	for(Entry<String, TabsChannel> entry : tabs.entrySet()) {
    	    TabsChannel tabsChannel = entry.getValue();
    		if (tabsChannel.getPosition() == position)
    		{
    			Log.i(TAG, "TabsManager get "+position+" zwrocono "+tabsChannel.getName());
    			return tabsChannel.getFragment();
    		}
    	}
    	Log.w(TAG, "TabsManager get "+position+" zwrocono null");
    	return null;
    }
    
    public HashMap<String, TabsChannel> getAll()
    {
    	return tabs;
    }
    
    public TabsFragment getFromName(String channel)
    {
    	TabsChannel tabsChannel = tabs.get(channel);
    	if (tabsChannel != null)
    	{
    		return tabsChannel.getFragment();
    	}
    	else
    		return null;
    }
    
    public TabsFragment getActive()
    {
    	int position = this.tabsActivity.getCurrentItem();
    	return this.get(position);
    }
}
