package com.mindflakes.TeamRED.AndRedMenu;

import java.io.FileNotFoundException;

import org.joda.time.DateTime;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.mindflakes.TeamRED.MenuXML.Reader;

public class QuickViewActivity extends TabActivity {
	
	private static final int QUICK_VIEW = 1234;
	private static final int MAIN_VIEW = 1235;
	private static final int SETTINGS = 1236;

	private Resources res;
	private TabHost mTabHost;
	private MealMenuDBAdapter  mDbAdapter;
//	private NotesDbAdapter mDbAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	res = getResources();
    	setContentView(R.layout.quick_view);
    	mTabHost = getTabHost();

    	try{
    		mDbAdapter = new MealMenuDBAdapter(this);
    		mDbAdapter.open();
    	} catch(Exception e){
    	}
    	//            readMenus();

    	addTabs();
    	mTabHost.setCurrentTab(0);
    }
    
    
    
	@Override
	protected void onResume() {
		super.onResume();
		int currentTab = mTabHost.getCurrentTab();
		if(mTabHost==null) mTabHost=getTabHost();
		mTabHost.setCurrentTab(0);
		mTabHost.clearAllTabs();
		addTabs();
		mTabHost.setCurrentTab(currentTab);
	}


	private void addTabs(){
		TabHost.TabSpec spec;  // Reusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab
		long millis = new DateTime().getMillis();

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MenuViewActivity.class);
		intent.putExtra(MenuViewActivity.KEY_MODE, MenuViewActivity.MODE_COMMONS);
		intent.putExtra(MealMenuDBAdapter.KEY_MEALMENU_NAME,res.getString(R.string.commons_name_short_carrillo));
			
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = mTabHost.newTabSpec(res.getString(R.string.commons_name_short_carrillo)+millis)
		.setIndicator(res.getString(R.string.commons_name_short_carrillo))
		.setContent(intent);
		mTabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, MenuViewActivity.class);
		intent.putExtra(MenuViewActivity.KEY_MODE, MenuViewActivity.MODE_COMMONS);
		intent.putExtra(MealMenuDBAdapter.KEY_MEALMENU_NAME,res.getString(R.string.commons_name_short_dlg));
		spec = mTabHost.newTabSpec(res.getString(R.string.commons_name_supershort_dlg)+millis)
		.setIndicator(res.getString(R.string.commons_name_supershort_dlg))
		.setContent(intent);
		mTabHost.addTab(spec);


		intent = new Intent().setClass(this, MenuViewActivity.class);
		intent.putExtra(MenuViewActivity.KEY_MODE, MenuViewActivity.MODE_COMMONS);
		intent.putExtra(MealMenuDBAdapter.KEY_MEALMENU_NAME,res.getString(R.string.commons_name_short_ortega));
		spec = mTabHost.newTabSpec(res.getString(R.string.commons_name_short_ortega)+millis)
		.setIndicator(res.getString(R.string.commons_name_short_ortega))
		.setContent(intent);
		mTabHost.addTab(spec);

		intent = new Intent().setClass(this, MenuViewActivity.class);
		intent.putExtra(MenuViewActivity.KEY_MODE, MenuViewActivity.MODE_COMMONS);
		intent.putExtra(MealMenuDBAdapter.KEY_MEALMENU_NAME,res.getString(R.string.commons_name_short_portola));
		spec = mTabHost.newTabSpec(res.getString(R.string.commons_name_short_portola)+millis)
		.setIndicator(res.getString(R.string.commons_name_short_portola))
		.setContent(intent);
		mTabHost.addTab(spec);

		mTabHost.setCurrentTab(0);

	}
    
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, QUICK_VIEW, 0, res.getString(R.string.quick_view));
        menu.add(0, MAIN_VIEW, 0, res.getString(R.string.main_view));
        menu.add(0, SETTINGS, 0, res.getString(R.string.settings));
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case SETTINGS:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        case QUICK_VIEW:
            return true;
        case MAIN_VIEW:
            Intent i4 = new Intent(this, MainViewActivity.class);
            startActivity(i4);
            return true;
       	
        }
        return false;
    }
    
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_SEARCH){
			Intent i3 = new Intent(this, SearchActivity.class);
        	startActivity(i3);
        	return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
		}
		// TODO Auto-generated method stub
		return super.onKeyLongPress(keyCode, event);
	}
    

}