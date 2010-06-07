package com.mindflakes.TeamRED.AndRedMenu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.mindflakes.TeamRED.MenuXML.Reader;

public class SettingsActivity extends Activity {
	MealMenuDBAdapter mDbAdapter;
	Resources res;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		res = getResources();
		mDbAdapter = new MealMenuDBAdapter(this);
		mDbAdapter.open();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_view);
		OnClickListener radio_listener = new OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				RadioButton rb = (RadioButton) v;
				PrintStream ps;
				try {
					ps = new PrintStream(openFileOutput(res.getString(R.string.menu_preference_file),MODE_PRIVATE));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					ps=null;
					e.printStackTrace();
				}
				if(rb.getText().toString().equals(res.getString(R.string.select_full))) ps.println(0);
				else if(rb.getText().toString().equals(res.getString(R.string.select_vgt))) ps.println(1);
				else if(rb.getText().toString().equals(res.getString(R.string.select_vegan))) ps.println(2);
				ps.close();
			}
		};

		final RadioButton radio_full = (RadioButton) findViewById(R.id.radio_full);
		final RadioButton radio_vegan = (RadioButton) findViewById(R.id.radio_vegan);
		final RadioButton radio_veget = (RadioButton) findViewById(R.id.radio_vegetarian);
		radio_full.setOnClickListener(radio_listener);
		radio_vegan.setOnClickListener(radio_listener);
		radio_veget.setOnClickListener(radio_listener);
	}


	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1234, 0, res.getString(R.string.clear_sql));
		menu.add(0, 1235, 0, res.getString(R.string.update_menus));
		return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1234:
			mDbAdapter.clear();
			return true;
		case 1235:
			updateMenuFiles();
			return true;
		}
		return false;
	}



	private void updateMenuFiles(){
		try{
			URL remoteFile = new URL(getResources().getString(R.string.serialized_menus_remote));

			HttpURLConnection c = (HttpURLConnection) remoteFile.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			FileOutputStream f = openFileOutput(getResources().getString(R.string.local_file_serialized_zipped),MODE_PRIVATE);
			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ( (len1 = in.read(buffer)) > 0 ) {
				f.write(buffer,0, len1);
			}
			f.close();
			in.close();
			Reader.uncompressFile(openFileInput(getResources().getString(R.string.local_file_serialized_zipped)),
					openFileOutput(getResources().getString(R.string.local_file_serialized),MODE_PRIVATE));
			loadMenusToSQL();
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	private void loadMenusToSQL() throws FileNotFoundException, NotFoundException{
		mDbAdapter.clear();
		mDbAdapter.addMenus(Reader.readSerialized(openFileInput(getResources().getString(R.string.local_file_serialized))));
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
