package com.mindflakes.TeamRED.AndRedMenu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchActivity extends Activity {
	private static final int QUICK_VIEW = 1234;
	private static final int MAIN_VIEW = 1235;
	private static final int SETTINGS = 1236;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		final Spinner spinner = (Spinner) findViewById(R.id.spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.commons_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    spinner.setAdapter(adapter);
	    
	    final EditText ef = (EditText)findViewById(R.id.edittext);
	    
	    Button submitbutton = (Button) findViewById(R.id.submit);
	    submitbutton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Intent i2 = new Intent(v.getContext(), SearchMenuListActivity.class);
	        	try{
		        	i2.putExtra(MealMenuDBAdapter.KEY_MEALMENU_NAME, (String)spinner.getSelectedItem());

	        	}catch(ClassCastException e){
		        	i2.putExtra(MealMenuDBAdapter.KEY_MEALMENU_NAME, ((TextView)spinner.getSelectedItem()).toString());
	        	}
	        	i2.putExtra(MealMenuDBAdapter.KEY_FOODITEM_NAME, ef.getText().toString());
	            startActivity(i2);
	        }
	      });

	    
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
            Intent i4 = new Intent(this, MainViewActivity.class);
            startActivity(i4);
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
        	return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
