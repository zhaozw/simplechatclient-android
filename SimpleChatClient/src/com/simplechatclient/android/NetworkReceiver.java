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
        Log.i(TAG, "NetworkReceiver onReceive");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String data = bundle.getString("message");
            String command = bundle.getString("command");

            if (command.equalsIgnoreCase("kernel")) {
                // kernel
                OnetKernel.getInstance().parse(data);
            } else if (command.equalsIgnoreCase("auth")) {
                // auth
                Messages.getInstance().showMessageAll("Połączono");

                // auth
                try {
                    OnetAuth.getInstance().authorize();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
    }
}
