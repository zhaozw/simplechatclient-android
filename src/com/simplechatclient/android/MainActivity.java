
package com.simplechatclient.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

        OnetAuth a = new OnetAuth();
        a.authorize("scc_test", "abc123");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
