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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.simplechatclient.android.NetworkService;

public class Network {
    private static final String TAG = "Network";

    private Context context;

    private NetworkService mBoundService;
    private boolean mIsBound = false;

    private static Network instance = new Network();
    public static synchronized Network getInstance() {return instance; }

    private Network()
    {
        super();
    }

    public void setActivity(Context context)
    {
        this.context = context;
    }

    public void send(String data)
    {
        Log.i("Network", "send: "+data);

        if (mIsBound && mConnection != null)
            mBoundService.send(data);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((NetworkService.LocalBinder)service).getService();

            Log.i("service connection", "service connected");
            // Tell the user about this for our demo.

            startHandler.sendEmptyMessage(0);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            Log.i("service connection", "service disconnected");
            mBoundService = null;
        }
    };

    private Handler startHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mIsBound && mBoundService != null)
                mBoundService.start(context);
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
        if (mIsBound)
            return mBoundService.isConnected();
        else
            return false;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
