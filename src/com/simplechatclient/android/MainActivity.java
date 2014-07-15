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

package com.simplechatclient.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.core.Network;
import com.onet.OnetAuth;

public class MainActivity extends Activity {
    //private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        //editText = (EditText)findViewById(R.id.editTextInput);
        //http://developer.android.com/training/implementing-navigation/lateral.html#horizontal-paging
        //http://developer.android.com/reference/android/support/v4/view/ViewPager.html

        // http://developer.android.com/training/implementing-navigation/nav-drawer.html
        
        /*
        Button connectButton = (Button)findViewById(R.id.button_connect);
        
        OnClickListener oclBtnOk = new OnClickListener() {
            @Override
            public void onClick(View v) {
            	//connect();
              // 
            	Intent intent = new Intent(this, ProfilesManager.class);
                startActivity(intent);

              
            }
          };
          
        connectButton.setOnClickListener(oclBtnOk);
        */
        
        
    }

    public void button_connect(View v)
    {
    	Intent intent = new Intent(this, ProfilesManager.class);
        startActivity(intent);    	
    }
    
    private void connect()
    {
        Network.getInstance().connect();

        OnetAuth a = new OnetAuth();
        a.authorize("scc_test", "");
        
        Network.getInstance().send("JOIN #scc");
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    */
}
