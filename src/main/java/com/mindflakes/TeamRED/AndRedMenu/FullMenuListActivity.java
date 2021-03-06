package com.mindflakes.TeamRED.AndRedMenu;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public  class FullMenuListActivity extends ListActivity {

	private static final int QUICK_VIEW = 1234;
	private static final int MAIN_VIEW = 1235;
	private static final int SETTINGS = 1236;

	private MealMenuDBAdapter mDbAdapter;
	private String common;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Bundle extras = getIntent().getExtras();            
		common = extras.getString(MealMenuDBAdapter.KEY_MEALMENU_NAME);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDbAdapter = new MealMenuDBAdapter(this);
		mDbAdapter.open();
		setContentView(R.layout.menu_list);
		fillData();
	}

	private void fillData() {
		Cursor c = mDbAdapter.fetchMenusForMainList(common);
		startManagingCursor(c);
		String[] from = new String[] { MealMenuDBAdapter.KEY_MEALMENU_MEALNAME,
				MealMenuDBAdapter.KEY_MEALMENU_STARTSTRING };
		int[] to = new int[] { R.id.maintext1, R.id.maintext2 };
		SimpleCursorAdapter menus = new SimpleCursorAdapter(this,
				R.layout.main_row, c, from, to);
		setListAdapter(menus);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, MenuViewActivity.class);
		i.putExtra(MenuViewActivity.KEY_MODE, MenuViewActivity.MODE_ROWID);
		i.putExtra(MealMenuDBAdapter.KEY_ROWID,id);
		startActivity(i);
	}
	
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
    	Resources res = getResources();
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
        	Intent i2 = new Intent(this, QuickViewActivity.class);
            startActivity(i2);
            return true;
        case MAIN_VIEW:
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
			Intent i2 = new Intent(this, QuickViewActivity.class);
            startActivity(i2);
            return true;
		}
		// TODO Auto-generated method stub
		return super.onKeyLongPress(keyCode, event);
	}

}
