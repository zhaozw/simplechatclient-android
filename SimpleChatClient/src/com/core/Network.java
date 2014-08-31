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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.onet.OnetAuth;
import com.onet.OnetKernel;

public class Network {
    private static final String TAG = "Network";

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    private String server = "czat-app.onet.pl";
    private int port = 5015;

    private NetworkThread networkThread;
    private NetworkHandler networkHandler;

    private static Network instance = new Network();
    public static synchronized Network getInstance() {return instance; }

    // TODO jak network bedzie Servicem to mozna stworzyc Intent do auth 
    // TODO dla tymczasowego nicka http://stackoverflow.com/a/3607934
    private Network()
    {
        super();
                
        networkThread = new NetworkThread();
        networkHandler = new NetworkHandler();
        in = null;
        out = null;
    }
    
    public void send(String data)
    {
        Log.i(TAG, data);
        Messages.getInstance().showMessage("Status", String.format("%s\r\n", data));
        
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
    
    public void connect()
    {
		try
		{
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
	    	if ((networkThread.isAlive()) || (!socket.isClosed()))
	    		networkThread.stop();
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
	    		networkThread.stop();
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

    static class NetworkHandler extends Handler {
        
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String data = bundle.getString("network_message");

            Log.i(TAG, data);
            Messages.getInstance().showMessage("Status", String.format("-> %s\r\n", data));

            OnetKernel.getInstance().parse(data);
        }
    };

	// TODO java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()

    class NetworkThread extends Thread {
        public void run() {
        	Log.i(TAG, "Network thread started");
            try
            {
                try
                {
                    InetAddress serverAddr = InetAddress.getByName(server);
                    socket = new Socket(serverAddr, port);
                
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    // auth
                    try
                    {
                    	OnetAuth.getInstance().authorize();
                    }
                    catch (Exception e)
                    {
                    	Log.e(TAG, e.toString());
                    	e.printStackTrace();
                    }
                    
                    String line = null;
                    while ((line = in.readLine()) != null)
                    {
                        if (line.length() != 0)
                        {
                            Message msg =  new Message();
                            Bundle bundle = new Bundle();
    
                            bundle.putString("network_message", line);
                            msg.setData(bundle);
    
                            networkHandler.sendMessage(msg);
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
            
            Log.i(TAG, "Network thread closed");
        }
    }
}


