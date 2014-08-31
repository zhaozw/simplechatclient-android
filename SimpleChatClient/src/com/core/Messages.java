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

import com.simplechatclient.android.TabsFragment;
import com.simplechatclient.android.TabsManager;


public class Messages {
    private static Messages instance = new Messages();
    public static synchronized Messages getInstance() {return instance; }
    
	public static final int NOTICE_INFO = 100 | 0x070000;
	public static final int NOTICE_WARNING = 101 | 0x070000;
	public static final int NOTICE_ERROR = 102 | 0x070000;
	public static final int NOTICE_QUESTION = 103 | 0x070000;

    public void showMessage(String channel, String data)
    {
    	TabsFragment fragment = TabsManager.getInstance().getFromName(channel);
    	if (fragment != null)
    		fragment.addMessage(data);
    }
    
    public void showMessageAll(String data)
    {
    }

    public void showMessageActive(String data)
    {
    	TabsFragment fragment = TabsManager.getInstance().getActive();
    	if (fragment != null)
    		fragment.addMessage(data);
    }
}
