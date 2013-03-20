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
    private String[] data;

    private static OnetKernel instance = new OnetKernel();
    public static synchronized OnetKernel getInstance() { return instance; }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
    
    public void parse(String message)
    {
        data = message.split(" ");
        
        if (data.length <= 1)
            return;

        String cmd0 = data[0];
        String cmd1 = data[1];
        
        if (cmd0.equalsIgnoreCase("ping"))
            raw_ping();
        else if (cmd0.equalsIgnoreCase("error"))
            raw_error();
        
        if (cmd1.equalsIgnoreCase("001"))
            raw_001();
        else if (cmd1.equalsIgnoreCase("801"))
            raw_801();
    }
    
    // PING :cf1f1.onet
    private void raw_ping()
    {
        String server = data[1];
        
        Network.getInstance().send(String.format("PONG %s", server));
    }
    
    // ERROR :Closing link (unknown@95.48.183.154) [Registration timeout]
    private void raw_error()
    {
    }
    
    // :cf1f4.onet 001 scc_test :Welcome to the OnetCzat IRC Network scc_test!51976824@83.28.35.219
    private void raw_001()
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
        
        if (Network.getInstance().isConnected())
        {
            Network.getInstance().send(String.format("NICK %s", nick));
            Network.getInstance().send(String.format("AUTHKEY %s", auth));
            Network.getInstance().send(String.format("USER * %s czat-app.onet.pl :%s", UOKey, nick));
        }
    }
}
