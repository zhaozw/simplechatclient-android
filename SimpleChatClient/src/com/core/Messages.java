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

package com.core;

import android.widget.ListView;

import com.simplechatclient.android.R;
import com.simplechatclient.android.TabsManager;


public class Messages {
    private static Messages instance = new Messages();
    public static synchronized Messages getInstance() {return instance; }
    
    public void showMessage(String channel, String data)
    {
    	//ListView listView = TabsManager.getInstance().get(channel).getView().findViewById(R.id.listView1);
    	//listView.
    	
    }
    
    public void showMessageAll(String data)
    {
    }
}
