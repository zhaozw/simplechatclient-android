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

import java.util.HashMap;
import java.util.Map.Entry;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.simplechatclient.android.MainActivity.SectionsPagerAdapter;

public class TabsManager {
    private static TabsManager instance = new TabsManager();
    public static synchronized TabsManager getInstance() {return instance; }

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
    	TabsChannel NewTabChannel = new TabsChannel();
    	NewTabChannel.setAvatar(null);
    	NewTabChannel.setDisplayedOptions(false);
    	NewTabChannel.setName(channel);
    	NewTabChannel.setFragment(new TabsFragment());
    	NewTabChannel.setOffline(false);
    	NewTabChannel.setPosition(tabs.size());
    	
    	tabs.put(channel, NewTabChannel);
    	
    	this.tabsActivity.add(channel);
    }

    public void remove(String channel)
    {
    	// TODO
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
    
    public Fragment get(int position)
    {
    	for(Entry<String, TabsChannel> entry : tabs.entrySet()) {
    	    TabsChannel tabsChannel = entry.getValue();
    		if (tabsChannel.getPosition() == position)
    			return tabsChannel.getFragment();
    	}
    	return null;
    }
    
    public Fragment get(String channel)
    {
    	return tabs.get(channel).getFragment();
    }
}
