
package com.core;
/*
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class Network extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "NETWORK";

    private Socket socket; // Network Socket

    private InputStream nis; // Network Input Stream

    private OutputStream nos; // Network Output Stream

    @Override
    protected Boolean doInBackground(Void... params) {

    }

    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }

    private void connect() {
        String server = "czat-app.onet.pl";
        int port = 5015;

        boolean conected = false;

        try {
            InetAddress serverAddr = InetAddress.getByName(server);
            socket = new Socket(serverAddr, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new
        InputStreamReader(socket.getInputStream()));
        
        finally {
        try {
        	nis.close();
        	nos.close();
        	nsocket.close();
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }

        in.readLine();
    }

    public void send(String data) {
        try {
            if (socket.isConnected()) {
                nos.write(data.getBytes());
            } else {
                Log.e(TAG, "SendDataToNetwork: Cannot send message. Socket is closed");
            }
        } catch (IOException e) {
            Log.e(TAG, "SendDataToNetwork: Message send failed. Caught an exception");
        }
    }
}
*/
