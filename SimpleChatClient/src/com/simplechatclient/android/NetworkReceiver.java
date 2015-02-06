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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.core.Messages;
import com.onet.OnetAuth;
import com.onet.OnetKernel;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkReceiver";

    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String data = bundle.getString("message");
            String command = bundle.getString("command");

            Log.i(TAG, "NetworkReceiver onReceive: "+command+" "+data);

            try {
                if (command.equalsIgnoreCase("kernel")) {
                    // kernel
                    OnetKernel.getInstance().parse(data);
                } else if (command.equalsIgnoreCase("auth")) {
                    // auth
                    Messages.getInstance().showMessageAll("Połączono");
                    OnetAuth.getInstance().authorize();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }
}
