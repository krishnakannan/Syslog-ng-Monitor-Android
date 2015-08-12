/*

	This program is free software: you can redistribute it and/or modify

    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */

package com.mobile.syslogng.monitor;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements IMainActivity {

	private String APP_NAME;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] menuItems;
	
	public static final String FRAGMENT_POS = "fragment_pos";
	public static final int WELCOME_FRAGMENT_POS = 0;
	public static final int IMPORT_CERTIFICATE_FRAGMENT_POS = 1;
	public static final int SYSLOGNG_FRAGMENT_POS = 2;
	public static final int MONITORED_SYSLOGNG_FRAGMENT_POS = 3;
	//public static final int ABOUT_FRAGMENT_POS = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		APP_NAME = getApplicationContext().getString(R.string.app_title);
		
		FileManager fManager = new FileManager(getApplicationContext());			
		fManager.createCertificateDirectory();	
		
		mTitle  = getTitle();
		mDrawerTitle = APP_NAME;
		menuItems = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menuItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  
				mDrawerLayout,         
				R.drawable.ic_drawer,  
				R.string.drawer_open,  
				R.string.drawer_close  
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); 
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); 
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			SQLiteManager sManager = new SQLiteManager(getApplicationContext());
			ArrayList<Syslogng> savedData = sManager.getSyslogngs();
			if(savedData.isEmpty()){
				loadFragment(WELCOME_FRAGMENT_POS);
			}
			else{
				loadFragment(MONITORED_SYSLOGNG_FRAGMENT_POS);
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return false;

	}


	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			loadFragment(position);
		}
	}

	private void loadFragment(int position) {
		
		
		
		
		
		switch(position)
		{
			case WELCOME_FRAGMENT_POS:
				setFragment(new WelcomeFragment(this,getApplicationContext()), position, "fragment_welcome_tag");
				break;
	
			case IMPORT_CERTIFICATE_FRAGMENT_POS:
				setFragment(new ImportCertificateFragment(this,getApplicationContext()), position, "fragment_importcert_tag");
				break;
				
			case SYSLOGNG_FRAGMENT_POS:
				setFragment(new SyslogngFragment(this,getApplicationContext(), null), position, "fragment_addsyslogng_tag");
				break;
				
			case MONITORED_SYSLOGNG_FRAGMENT_POS:
				setFragment(new MonitoredSyslogngFragment(this,getApplicationContext()), position, "fragment_monitored_syslogng_tag");
				break;
				
			
			
			default:
				break;
		}
		
		updateDrawer(position);
		setTitle(menuItems[position]);
		
	}
	
	private void setFragment(Fragment fragment, Integer position, String tag){
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Bundle args = new Bundle();
		args.putInt(MainActivity.FRAGMENT_POS, position);
		fragment.setArguments(args);
		transaction.replace(R.id.container, fragment, tag).commit();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void updateDrawer(Integer position){
		
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
	}
	

}
