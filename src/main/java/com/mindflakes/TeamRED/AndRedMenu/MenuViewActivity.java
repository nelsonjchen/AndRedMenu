package com.mindflakes.TeamRED.AndRedMenu;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mindflakes.TeamRED.menuClasses.FoodItem;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.menuClasses.Venue;

public class MenuViewActivity extends Activity {
	private static final int QUICK_VIEW = 1234;
	private static final int MAIN_VIEW = 1235;
	private static final int SETTINGS = 1236;

	
	public static final String KEY_MODE = "mode";
	public static final int MODE_ROWID = 0;
	public static final int MODE_COMMONS = 1;
	private MealMenuDBAdapter mDbAdapter;
	private Long mRowId;
	private String mCommons;
	private Integer mMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDbAdapter = new MealMenuDBAdapter(this);
		mDbAdapter.open();

        Bundle extras = getIntent().getExtras();            
		mMode = extras != null ? extras.getInt(KEY_MODE) 
									: null;
		switch(mMode){
		case MODE_ROWID:
			mRowId = extras.getLong(MealMenuDBAdapter.KEY_ROWID);
			break;
		case MODE_COMMONS:
			mCommons = extras.getString(MealMenuDBAdapter.KEY_MEALMENU_NAME);
			break;
		}

		
		setLayout();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setLayout();
	}
	
	private void setLayout(){
		MealMenu menu = null;
		DateTimeFormatter dtf = DateTimeFormat.forPattern("'on' EEE, MMM dd");
		int food_type=0;
		try {
			food_type = new Scanner(openFileInput("menu_preferences")).nextInt();
		} catch (FileNotFoundException e) {
			
		}
		if(mMode==MODE_ROWID){
			menu=mDbAdapter.fetchMenu(mRowId,food_type);
		} else if(mMode==MODE_COMMONS){
			menu=mDbAdapter.selectFirstMeal(mCommons, new DateTime().getMillis(),food_type);
		}
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		TextView text;
		if(menu!=null){
			if(mMode==MODE_ROWID){
				text = new TextView(this);
				text.setText(menu.getCommonsName());
				text.setGravity(Gravity.CENTER_HORIZONTAL);
				mainLayout.addView(text);
			}
			text = new TextView(this);
			text.setText(menu.getMealName()+" "+dtf.print(menu.getMealInterval().getStart()));
			text.setGravity(Gravity.CENTER_HORIZONTAL);
			mainLayout.addView(text);
		}
		
		ListView view = new ListView(this);
		ArrayList<String> toSet = toArrayHelper(menu);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.quick_row,toSet);
		view.setAdapter(adapter);
		
		mainLayout.addView(view);
		setContentView(mainLayout);
	}
	
    private ArrayList<String> toArrayHelper(MealMenu menu){
    	if(menu==null) return new ArrayList<String>();
    	ArrayList<String> arr = new ArrayList<String>();
    	for(Venue ven:menu.getVenues()){
    		arr.add(ven.getName());
    		for(FoodItem food:ven.getFoodItems()){
    			arr.add("       "+food.getName());
    		}
    	}
    	return arr;
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
        	if(mMode!=MODE_COMMONS){
        		Intent i2 = new Intent(this, QuickViewActivity.class);
        		startActivity(i2);
        	}
            return true;
        case MAIN_VIEW:
        	if(mMode!=MODE_ROWID){
        		Intent i4 = new Intent(this, MainViewActivity.class);
        		startActivity(i4);
        	}
            return true;
        }
        return false;
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

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_SEARCH){
			Intent i3 = new Intent(this, SearchActivity.class);
        	startActivity(i3);
        	return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
}
