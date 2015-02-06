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

package com.core;

import android.util.Log;

import java.util.HashMap;
import java.util.Map.Entry;

import com.simplechatclient.android.TabsChannel;
import com.simplechatclient.android.TabsFragment;
import com.simplechatclient.android.TabsManager;


public class Messages {
    private static Messages instance = new Messages();
    public static synchronized Messages getInstance() {return instance; }
    
    public static final int NOTICE_INFO = 100 | 0x070000;
    public static final int NOTICE_WARNING = 101 | 0x070000;
    public static final int NOTICE_ERROR = 102 | 0x070000;
    public static final int NOTICE_QUESTION = 103 | 0x070000;

    public enum MessageType {DEFAULT, JOIN, PART, QUIT, KICK, MODE, NOTICE, INFO, ME, ERROR, HIGHLIGHT, MODERNOTICE}

    public void showMessage(String channel, String data)
    {
        Log.i("Messages show", "channel: " + channel + " data: " + data);
        try
        {
            TabsFragment fragment = TabsManager.getInstance().getFromName(channel);
            if (fragment != null)
                fragment.addMessage(data);
            else
                Log.w("Messages show", "Missing fragment for channel "+channel+" data: "+data);
        }
        catch (NullPointerException e) {}
    }
    
    public void showMessageAll(String data)
    {
        Log.i("Messages show all", "data: " + data);
        HashMap<String, TabsChannel> tabs = TabsManager.getInstance().getAll();
        for(Entry<String, TabsChannel> entry : tabs.entrySet()) {
            try
            {
                TabsChannel tabsChannel = entry.getValue();
                TabsFragment fragment = tabsChannel.getFragment();
                if (fragment != null)
                    fragment.addMessage(data);
                else
                    Log.w("Messages show all", "Missing fragment for channel "+tabsChannel.getName()+" data: "+data);
            }
            catch (NullPointerException e) {}
        }
    }

    public void showMessageActive(String data)
    {
        Log.i("Messages show active", "data: " + data);
        try
        {
            TabsFragment fragment = TabsManager.getInstance().getActive();
            if (fragment != null)
                fragment.addMessage(data);
            else
                Log.w("Messages show active", "Missing fragment for active channel data: "+data);
        }
        catch (NullPointerException e) {}
    }
}
