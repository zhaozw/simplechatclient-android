package com.simplechatclient.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class ProfilesFragment extends Fragment {

	public static ProfilesFragment newInstance() {
		ProfilesFragment fragment = new ProfilesFragment();
		return fragment;
	}

	public ProfilesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profiles_fragment, container, false);
		return rootView;
	}
}