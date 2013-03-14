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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.onet.OnetUtils;

public class MainActivity extends Activity {
    EditText editText;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        editText = (EditText)findViewById(R.id.editTextInput);
        textView = (TextView)findViewById(R.id.textView1);
        String message = editText.getText().toString();

        textView.append(message);

        // OnetAuth a = new OnetAuth();
        // a.authorize("scc_test", "");

        OnetUtils u = new OnetUtils();
        String test = u.transform("6cCWOjkb3Zm2HpXr");

        Log.e("SCC", "Klucz auth: " + test);

        if (test.equals("GCCG4B8IAxvGCuxw"))
            Log.e("SCC", "Klucz auth poprawny!");
        else
            Log.e("SCC", "Klucz auth bledny!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
