package com.simplechatclient.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.core.Network;
import com.core.Settings;

class LoginFragment extends Fragment {

	public static LoginFragment newInstance() {
		LoginFragment fragment = new LoginFragment();
		return fragment;
	}

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.login_fragment, container, false);
		
		TextView nickText = (TextView)rootView.findViewById(R.id.textViewNick);
        nickText.setText(Settings.getInstance().get("nick"));

		return rootView;
	}
	
	public void button_login()
	{
		Network.getInstance().connect();

		//Intent intent = new Intent(this, ChannelsActivity.class);
        //startActivity(intent);  
	}
}