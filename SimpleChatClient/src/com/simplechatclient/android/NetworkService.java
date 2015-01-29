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

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.core.Messages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkService extends Service {

    private static final String TAG = "NetworkService";

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private String server = "czat-app.onet.pl";
    private int port = 5015;

    private Thread networkThread;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public NetworkService getService() {
            return NetworkService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private String utfToIso(String data)
    {
        String result = "";

        try {
            result = new String(data.getBytes(), "ISO-8859-2");
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "UnsupportedEncodingException: "+data);
        }

        return result;
    }

    private String isoToUtf(String data)
    {
        String result = "";

        try {
            result = new String(data.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "UnsupportedEncodingException: "+data);
        }

        return result;
    }

    public boolean isConnected()
    {
        return socket.isConnected();
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

    @Override
    public void onCreate() {
        Log.w("NetworkService", "onCreate");
    }

    public void start(Context context)
    {
        final Context sharedContext = context;

        Runnable r = new Runnable() {
            public void run() {

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
                        sharedContext.sendBroadcast(intentAuth);

                        String line;
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
                                sharedContext.sendBroadcast(intentKernel);
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

                Log.i(TAG, "Network closed");

                stopSelf();
            }
        };

        networkThread = new Thread(r);
        networkThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("NetworkService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if (socket.isConnected())
            this.send("QUIT");

        Log.w("NetworkService", "Stopped");

        networkThread.interrupt();
    }
}
