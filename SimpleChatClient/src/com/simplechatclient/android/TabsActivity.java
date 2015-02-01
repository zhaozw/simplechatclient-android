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

import java.util.Locale;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.core.Config;
import com.core.Network;
import com.core.Settings;
import com.database.DatabaseProfile;
import com.database.DatabaseSetting;
import com.models.Channels;

public class TabsActivity extends ActionBarActivity implements ActionBar.TabListener {

	//private static final String TAG = "TabsActivity";
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerTabsAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	ActionBar actionBar;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private int notificationId = 7898291;

    public TabsActivity()
	{
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_activity);

        Log.i("Tabs activity", "onCreate");

		// Set up the action bar.
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerTabsAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pagerTabs);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// read profile
		// settings
		Config current_config = new Config(getApplicationContext());
        
        DatabaseSetting current_settings = current_config.getSetting();
        DatabaseProfile current_profile = current_config.getProfile(current_settings.getCurrent_profile());
        
        Settings.getInstance().set("current_profile", Integer.toString(current_settings.getCurrent_profile()));
        Settings.getInstance().set("unique_id", current_settings.getUnique_id());
        
        Settings.getInstance().set("nick", current_profile.getNick());
        Settings.getInstance().set("password", current_profile.getPassword());
        Settings.getInstance().set("font", current_profile.getFont());
        Settings.getInstance().set("bold", current_profile.getBold());
        Settings.getInstance().set("italic", current_profile.getItalic());
        Settings.getInstance().set("color", current_profile.getColor());

        // notification
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getText(R.string.notification_disconnected))
                .setAutoCancel(false)
                .setOngoing(true);

        Intent resultIntent = new Intent(this, TabsActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(pendingIntent);

        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());

        // network
        Network.getInstance().setParameters(this.getApplicationContext(), mBuilder, mNotificationManager);

        // tabs manager
		TabsManager.getInstance().setTabsActivity(this);

        // status
        TabsManager.getInstance().add(Channels.STATUS);
        this.add(Channels.STATUS);

        // TODO tutaj dodac dodawanie wszystkich tabow albo jakis rodzaj zapamietywania instancji

		// is first run
		if (Settings.getInstance().get("first_run") == "true")
		{
            Log.i("TabsActivity", "first run");

			// first run false
			Settings.getInstance().set("first_run", "false");

			Intent profileListIntent = new Intent(this, ProfileActivity.class);
	    	profileListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	profileListIntent.putExtra("tab", "0"); // main
	        startActivity(profileListIntent);
		}
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Tabs activity", "onStart");
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("Tabs activity", "onDestroy");
		//Network.getInstance().disconnect();
		TabsManager.getInstance().removeAll();

        mNotificationManager.cancel(notificationId);

        Settings.getInstance().clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Tabs activity", "onResume");
    }

    public void add(String channel)
	{
		actionBar.addTab(actionBar.newTab()
				.setText(channel)
				.setTabListener(this));
		
		mViewPager.getAdapter().notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tabs, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.

        Log.i("Tabs activity", "onTabSelected "+tab.getPosition());

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public int getCurrentItem()
	{
		return mViewPager.getCurrentItem();
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerTabsAdapter extends FragmentPagerAdapter {

		public SectionsPagerTabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
            Log.i("SectionsPagerTabsAdapter", "get item: "+position);

			return TabsManager.getInstance().get(position);
		}

		@Override
		public int getCount() {
			return TabsManager.getInstance().count();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			return TabsManager.getInstance().getName(position).toUpperCase(l);
		}
	}
}
