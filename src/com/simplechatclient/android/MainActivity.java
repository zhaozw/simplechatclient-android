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

package com.simplechatclient.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.core.Network;
import com.onet.OnetAuth;

public class MainActivity extends Activity {
    //private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //editText = (EditText)findViewById(R.id.editTextInput);
        //http://developer.android.com/training/implementing-navigation/lateral.html#horizontal-paging
        //http://developer.android.com/reference/android/support/v4/view/ViewPager.html

        connect();
    }

    private void connect()
    {
        Network.getInstance().connect();

        OnetAuth a = new OnetAuth();
        a.authorize("scc_test", "");
    }
/*
    private void disconnect()
    {
    	Network.getInstance().disconnect();
    }
*/
    /*
    public void sendMessage(View view) {
        String message = editText.getText().toString();

        //textView.append(message);
        Network.getInstance().send(message);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
