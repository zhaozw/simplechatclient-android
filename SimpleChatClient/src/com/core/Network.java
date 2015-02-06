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

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.simplechatclient.android.NetworkService;

public class Network {
    private static final String TAG = "Network";

    private Context context;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private NetworkService mBoundService;
    private boolean mIsBound = false;

    private static Network instance = new Network();
    public static synchronized Network getInstance() {return instance; }

    private Network()
    {
        super();
    }

    public void setParameters(Context context, NotificationCompat.Builder mBuilder, NotificationManager mNotificationManager)
    {
        this.context = context;
        this.mBuilder = mBuilder;
        this.mNotificationManager = mNotificationManager;
    }

    public void send(String data)
    {
        Log.i("Network", "send: "+data);

        if (mIsBound && mConnection != null)
            mBoundService.send(data);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            mBoundService = ((NetworkService.LocalBinder)service).getService();

            Log.i("service connection", "service connected");

            startHandler.sendEmptyMessage(0);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i("service connection", "service disconnected");
            mBoundService = null;
        }
    };

    private Handler startHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mIsBound && mBoundService != null)
                mBoundService.start(context, mBuilder, mNotificationManager);
        }
    };

    public void connect()
    {
        if (!mIsBound)
        {
            mIsBound = true;

            context.bindService(new Intent(context, NetworkService.class), mConnection, Context.BIND_AUTO_CREATE);
        }
        else
            Log.w(TAG, "Network Connect - error already connected");
    }
    
    public void disconnect()
    {
        if (mIsBound) {
            mIsBound = false;

            context.unbindService(mConnection);
        }
        else
            Log.w(TAG, "Network disconnect - error already disconnected");
    }

    public boolean isConnected()
    {
        return mIsBound && mBoundService.isConnected();
    }

    // TODO http://stackoverflow.com/a/10350511
    /*
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    */
}
