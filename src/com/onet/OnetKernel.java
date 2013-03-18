/*
 * Simple Chat Client
 *
 *   Copyright (C) 2013 Piotr Łuczko <piotr.luczko@gmail.com>
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

package com.onet;

import com.core.Network;
import com.core.Settings;

public class OnetKernel {
	private Network network;
	private String[] data;
	
	public OnetKernel(Network n)
	{
		network = n;
	}

	public void parse(String message)
	{
		data = message.split(" ");
		
		if (data.length <= 1)
			return;

		String cmd0 = data[0];
		String cmd1 = data[1];
		
		//if (cmd1.equalsIgnoreCase("notice"))
			//raw_notice();
		if (cmd1.equalsIgnoreCase("801"))
			raw_801();
	}
	
	private void raw_notice()
	{
	}
	
	// :cf1f3.onet 801 scc_test :q5VMy1wl6hKL5ZUt
	// :cf1f2.onet 801 384-unknown :mIGlbZP0R056xedZ
	private void raw_801()
	{
		String key = data[3].substring(1);

		String auth = OnetUtils.transform(key);
		String nick = Settings.getInstance().get("nick");
		String UOKey = Settings.getInstance().get("uo_key");
		
	    if (network.isConnected())
        {
        	network.send(String.format("NICK %s", nick));
        	network.send(String.format("AUTHKEY %s", auth));
        	network.send(String.format("USER * %s czat-app.onet.pl :%s", UOKey, nick));
        }
	}
}
