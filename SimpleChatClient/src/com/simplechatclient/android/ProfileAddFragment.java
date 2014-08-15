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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

class ProfileAddFragment extends Fragment {

	private Context context;
	private View view;
	
	public static ProfileAddFragment newInstance() {
		ProfileAddFragment fragment = new ProfileAddFragment();
		return fragment;
	}

	public ProfileAddFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.profile_add_fragment, container, false);
		context = container.getContext();
		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		/*
				CompoundButton compoundButtonRegistered = (CompoundButton)findViewById(R.id.edit_profile_switch_registered);

			compoundButtonRegistered.setChecked(true);
		//compoundButtonRegistered.setOnClickListener(this);

		 */
	}
}
