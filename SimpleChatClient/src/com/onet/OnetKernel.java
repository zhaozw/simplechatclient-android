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

package com.onet;

import android.util.Log;

import com.core.Messages;
import com.core.Network;
import com.core.Settings;
import com.simplechatclient.android.TabsManager;

public class OnetKernel {
	private static final String TAG = "OnetKernel";
	
	private String[] data;

    private static OnetKernel instance = new OnetKernel();
    public static synchronized OnetKernel getInstance() { return instance; }

    public void parse(String message)
    {
        data = message.split(" ");
        
        if (data.length <= 2)
            return;

        String data0 = data[0];
        String data1 = data[1];
        
        if (data0.equalsIgnoreCase("ping")) { raw_ping(); return; }
        else if (data0.equalsIgnoreCase("error")) { raw_error(); return; }
        else if (data1.equalsIgnoreCase("pong")) { raw_pong(); return; }
        else if (data1.equalsIgnoreCase("privmsg")) { raw_privmsg(); return; }
        else if (data1.equalsIgnoreCase("join")) { raw_join(); return; }
        else if (data1.equalsIgnoreCase("mode")) { raw_mode(); return; }
        
        if (data1.equalsIgnoreCase("001")) { raw_001(); return; }
        else if (data1.equalsIgnoreCase("801")) { raw_801(); return; }

        if (data.length >= 4)
        {
	        String data3 = data[3];
	        if ((data1.equalsIgnoreCase("notice")) && (!data3.isEmpty()))
	        {
	        	if ((data3.length() != 4) || (data3.equalsIgnoreCase(":***")))
	        	{
	        		raw_notice();
	        		return;
	        	}
	        	else
	        	{
	        		if (data3.length() == 3)
	        		{
	        			int int3 = Integer.parseInt(data3);
	        			
	        			if (int3 == 141) { raw_141n(); return; }
	        			else if (int3 == 142) { raw_142n(); return; }
	        		}
	        	}
	        }
        }

        //Log.i(TAG, "Unknown RAW: "+message);
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
    	// TODO
    }
    
    // :cf1f4.onet PONG cf1f4.onet :1340185644095
    private void raw_pong()
    {
    	// TODO
    }

    // :scc_test!51976824@3DE379.B7103A.6CF799.6902F4 JOIN #Quiz :rx,0
    private void raw_join()
    {
    	String nick = data[0];
    	if (nick.startsWith(":")) nick = nick.substring(1);
    	nick = nick.substring(0, nick.indexOf("!"));
    	
    	String channel = data[2];

    	if (nick.equalsIgnoreCase(Settings.getInstance().get("nick")))
    	{
	    	// add
			TabsManager.getInstance().add(channel);

			// info
	    	if (!channel.startsWith("^"))
	    	{
	    		Network.getInstance().send(String.format("CS INFO %s i", channel));
	    	}
    	}
    }
    
	 // :Merovingian!26269559@2294E8.94913F.2EAEC9.11F26D PRIVMSG @#scc :hello
	 // :Merovingian!26269559@2294E8.94913F.2EAEC9.11F26D PRIVMSG %#scc :hello
	 // :Merovingian!26269559@2294E8.94913F.2EAEC9.11F26D PRIVMSG +#scc :hello
	 // :Merovingian!26269559@2294E8.94913F.2EAEC9.11F26D PRIVMSG #scc :hello
	 // :Merovingian!26269559@2294E8.94913F.2EAEC9.11F26D PRIVMSG ^scc_test :hello
    private void raw_privmsg()
    {
    	String nick = data[0];
    	if (nick.startsWith(":")) nick = nick.substring(1);
    	if (nick.contains("!")) nick = nick.substring(0, nick.indexOf("!"));
    	
    	String nickOrChannel = data[2];
    	
    	String message = new String();
    	for (int i = 3; i < data.length; ++i) { if (i != 3) message += " "; message += data[i]; }
    	if (message.startsWith(":")) message = message.substring(1);
    	
        String display = String.format("<%s> %s", nick, message);

    	// channel
        if (nickOrChannel.contains("#"))
        {
            String fullChannel = nickOrChannel;
            @SuppressWarnings("unused")
			String group = fullChannel.substring(fullChannel.indexOf("#"));
            
            String channel = fullChannel.substring(fullChannel.indexOf("#"));
            
            Messages.getInstance().showMessage(channel, display);
        }
        // priv
        else if (nickOrChannel.contains("^"))
        {
        	String channel = nickOrChannel;
        	
        	Messages.getInstance().showMessage(channel, display);
        }
        // notice
        else
        {
            Messages.getInstance().showMessageActive(display);
        }
    }
    
	 // :Llanero!43347263@admin.onet NOTICE #channel :test
	 // :cf1f2.onet NOTICE scc_test :Your message has been filtered and opers notified: spam #2480
	 // :Llanero!43347263@admin.onet NOTICE $* :458852 * * :%Fb%%C008100%Weź udział w Konkursie Mikołajkowym - skompletuj zaprzęg Świetego Mikołaja! Więcej info w Wieściach z Czata ! http://czat.onet.pl/1632594,wiesci.html
	 // :Panie_kierowniku!57643619@devel.onet NOTICE Darom :458852 * * :bum
	 // :Panie_kierowniku!57643619@devel.onet NOTICE Darom :458853 * * :bum
	 // :Panie_kierowniku!57643619@devel.onet NOTICE Darom :458854 * * :bum
	 // :Panie_kierowniku!57643619@devel.onet NOTICE Darom :458855 * * :bum
    private void raw_notice()
    {
    	String who = data[0];
    	if (who.startsWith(":")) who = who.substring(1);
    	if (who.contains("!")) who = who.substring(0, who.indexOf("!"));
    	
    	String nickOrChannel = data[2];
    	    	
    	String category = data[3];
    	if (category.startsWith(":")) category = category.substring(1);
    	
    	int iCategory = 0;
    	try { iCategory = Integer.parseInt(category); } catch (NumberFormatException e) {}

    	String message = new String();
    	String categoryString = new String();

    	switch (iCategory)
    	{
	        case Messages.NOTICE_INFO:
	            //if (categoryString.isEmpty()) categoryString = getResources().getString(R.string.notice_information)+": ";
	        case Messages.NOTICE_WARNING:
	            //if (categoryString.isEmpty()) categoryString = tr("Warning")+": ";
	        case Messages.NOTICE_ERROR:
	            //if (categoryString.isEmpty()) categoryString = tr("Error")+": ";
	        case Messages.NOTICE_QUESTION:
	            //if (categoryString.isEmpty()) categoryString = tr("Question")+": ";
	
	            for (int i = 6; i < data.length; ++i) { if (i != 6) message += " "; message += data[i]; }
	            if (message.startsWith(":")) message = message.substring(1);
	            break;
	        default:
	            for (int i = 3; i < data.length; ++i) { if (i != 3) message += " "; message += data[i]; }
	            if (message.startsWith(":")) message = message.substring(1);
	            break;
    	}
    	
        String display = String.format("-%s- %s%s", who, categoryString, message);
        Log.i(TAG, "notice display: "+display);
        
    	// channel
        if (nickOrChannel.contains("#"))
        {
            String fullChannel = nickOrChannel;
            @SuppressWarnings("unused")
			String group = fullChannel.substring(fullChannel.indexOf("#"));
            
            String channel = fullChannel.substring(fullChannel.indexOf("#"));
            
            Messages.getInstance().showMessage(channel, display);
        }
        // priv
        else if (nickOrChannel.contains("^"))
        {
        	String channel = nickOrChannel;
        	
        	Messages.getInstance().showMessage(channel, display);
        }
        // notice
        else
        {
            Messages.getInstance().showMessageActive(display);
        }
    }
    
	 // :Merovingian!26269559@jest.piekny.i.uroczy.ma.przesliczne.oczy MODE Merovingian :+b
	 // :Merovingian!26269559@2294E8.94913F.2EAEC9.11F26D MODE Merovingian :+b
	 // :ankaszo!51613093@F4C727.446F67.966AC9.BAAE26 MODE ankaszo -W
	 // :ChanServ!service@service.onet MODE #glupia_nazwa +k bum
	 // :ChanServ!service@service.onet MODE #bzzzz -l
	 // :NickServ!service@service.onet MODE scc_test +r
	 // :ChanServ!service@service.onet MODE #scc +ips
	 // :ChanServ!service@service.onet MODE #scc +o scc_test
	 // :ChanServ!service@service.onet MODE #scc +eo *!51976824@* scc_test
	 // :ChanServ!service@service.onet MODE #abc123 +il-e 1 *!51976824@*
    private void raw_mode()
    {
    	// TODO
    	
        // registered nick
    	/*
        if ((strNick == Settings.getInstance().get("nick")) && (strFlag == "+r"))
        {
            // channel homes
            Network.getInstance().send("CS HOMES");

            // get my stats
            Network.getInstance().send(String.format("RS INFO %s", Settings.getInstance().get("nick")));
        }
        */
    }
    
    // :cf1f4.onet 001 scc_test :Welcome to the OnetCzat IRC Network scc_test!51976824@83.28.35.219
    private void raw_001()
    {
    	Settings.getInstance().set("ignore_favourites", "false");
    	
    	// protocol
    	Network.getInstance().send("PROTOCTL ONETNAMESX");
    	
    	// channels list
    	//Network.getInstance().send("SLIST  R- 0 0 100 null");
    }

	 // NS FAVOURITES
	 // :NickServ!service@service.onet NOTICE scc_test :141 :#Scrabble #Quiz #scc
    private void raw_141n()
    {
    	// TODO
    }

	 // NS FAVOURITES
	 // :NickServ!service@service.onet NOTICE scc_test :142 :end of favourites list
    private void raw_142n()
    {
    	if (Settings.getInstance().get("ignore_favourites") == "false")
    	{
    		Settings.getInstance().set("ignore_favourites", "true");
    		
    		// TODO
    	}
    }

	 // CS HOMES
	 // :ChanServ!service@service.onet NOTICE scc_test :151 :h#scc
	 // NS OFFLINE
	 // :NickServ!service@service.onet NOTICE Merovingian :151 :jubee_blue
    private void raw_151n()
    {
    	// TODO
    }
    
	// CS HOMES
	// :ChanServ!service@service.onet NOTICE scc_test :152 :end of homes list
	// NS OFFLINE
	// :NickServ!service@service.onet NOTICE Merovingian :152 :end of offline senders list
    private void raw_152n()
    {
    	// TODO
    }
    
    // :cf1f3.onet 801 scc_test :q5VMy1wl6hKL5ZUt
    // :cf1f2.onet 801 384-unknown :mIGlbZP0R056xedZ
    private void raw_801()
    {
        String key = data[3];
        if (key.startsWith(":")) key = key.substring(1);

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
