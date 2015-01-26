/*
 * Simple Chat Client
 *
 *   Copyright (C) 2015 Piotr Łuczko <piotr.luczko@gmail.com>
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Network {
    private static final String TAG = "Network";

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    private String server = "czat-app.onet.pl";
    private int port = 5015;

    private NetworkThread networkThread;
    Context context;

    private static Network instance = new Network();
    public static synchronized Network getInstance() {return instance; }

    // TODO jak network bedzie Servicem to mozna stworzyc Intent do auth 
    // TODO dla tymczasowego nicka http://stackoverflow.com/a/3607934

    private Network()
    {
        super();

        networkThread = new NetworkThread();
        in = null;
        out = null;
    }

    public void setActivity(Context context)
    {
        this.context = context;
    }

    private String utfToIso(String data)
    {
    	String result = "";
    	
		try {
    		result = new String(data.getBytes(), "ISO-8859-2");
		} catch (UnsupportedEncodingException e) {
		}

		return result;
    }
    
    private String isoToUtf(String data)
    {   	
    	String result = "";
            	
		try {
    		result = new String(data.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		return result;
    }
    
    public void send(String data)
    {
        data = utfToIso(data);
        Log.i(TAG, "-> "+data);
    	
        try {
            if ((socket != null) && (socket.isConnected())) {
                out.write(String.format("%s\r\n", data));
                out.flush();
            } else {
                Log.w(TAG, "Send: Cannot send message: socket is closed");
            }
        } catch (IOException e) {
            Log.e(TAG, "Send: Cannot send message: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    private synchronized void stopThread(Thread theThread)
    {
        if (theThread != null)
        {
            theThread = null;
        }
    }

    public void connect()
    {    	
		try
		{
	    	Messages.getInstance().showMessageAll("Logowanie ...");

	        // TODO thread already started
	    	if ((!networkThread.isAlive()) || (socket.isClosed()))
	    		networkThread.start();
		}
		catch (IllegalThreadStateException e)
		{
			Log.e(TAG, "Illegal thread exception:" + e.getMessage());
            e.printStackTrace();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Network exception:" + e.getMessage());
            e.printStackTrace();
		}
    }
    
    public void disconnect()
    {
		try
		{
			Messages.getInstance().showMessageAll("Wylogowywanie ...");
			
	    	if ((networkThread.isAlive()) || (!socket.isClosed()))
	    	{
	    		this.send("QUIT");
	    		socket.close();
	    		this.stopThread(networkThread);
	    	}
		}
		catch (IllegalThreadStateException e)
		{
			Log.e(TAG, "Illegal thread exception:" + e.getMessage());
	        e.printStackTrace();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Network exception:" + e.getMessage());
            e.printStackTrace();
		}
    }
    
    public void reconnect()
    {
		try
		{
	    	if ((networkThread.isAlive()) || (socket.isClosed()))
	    	{
	    		this.send("QUIT");
	    		socket.close();
	    		this.stopThread(networkThread);
	    	}
	    	if ((!networkThread.isAlive()) || (!socket.isClosed()))
	    		networkThread.start();
		}
		catch (IllegalThreadStateException e)
		{
			Log.e(TAG, "Illegal thread exception:" + e.getMessage());
	        e.printStackTrace();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Network exception:" + e.getMessage());
            e.printStackTrace();
		}
    }
    
    public boolean isConnected()
    {
    	if (socket != null)
    		return socket.isConnected();
    	else
    		return false;
    }   

    class NetworkThread extends Thread {
        public void run() {
        	Log.i(TAG, "Network thread started");
            try
            {
                try
                {
                    InetAddress serverAddr = InetAddress.getByName(server);
                    socket = new Socket(serverAddr, port);
                
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-2"));
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-2"));

                    Intent intentAuth = new Intent();
                    intentAuth.setAction("com.simplechatclient.networkbroadcast");
                    intentAuth.putExtra("message", "");
                    intentAuth.putExtra("command", "auth");
                    context.sendBroadcast(intentAuth);

                    String line = null;
                    while ((line = in.readLine()) != null)
                    {
                        if (line.length() != 0)
                        {
                        	line = isoToUtf(line);
                        	Log.i(TAG, "<- "+line);

                            Intent intentKernel = new Intent();
                            intentKernel.setAction("com.simplechatclient.networkbroadcast");
                            intentKernel.putExtra("message", line);
                            intentKernel.putExtra("command", "kernel");
                            context.sendBroadcast(intentKernel);
                        }
                    }
                } catch (UnknownHostException e) {
                    Log.e(TAG, "Unknown host exception:" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if ((socket != null) && (!socket.isClosed()))
                        socket.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception:" + e.getMessage());
                e.printStackTrace();
            }

            Messages.getInstance().showMessageAll("Rozłączono");
            Log.i(TAG, "Network thread closed");
        }
    }
}


