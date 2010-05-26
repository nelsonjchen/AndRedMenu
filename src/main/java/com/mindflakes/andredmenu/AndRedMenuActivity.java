package com.mindflakes.andredmenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.mindflakes.TeamRED.MenuXML.Reader;
import com.mindflakes.TeamRED.menuClasses.MealMenu;

public class AndRedMenuActivity extends TabActivity {
	private TabHost mTabHost;
	private ArrayList<MealMenu> mMenus;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.quick_view);
            mTabHost = getTabHost();
            mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(getResources().getString(R.string.commons_name_short_carrillo)).setContent(R.id.quickview1));
            mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(getResources().getString(R.string.commons_name_short_dlg)).setContent(R.id.quickview2));
            mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(getResources().getString(R.string.commons_name_short_ortega)).setContent(R.id.quickview3));
            mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator(getResources().getString(R.string.commons_name_short_portola)).setContent(R.id.quickview4));
            mTabHost.setCurrentTab(0);
    }
    
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1234, 0, "New Game");
        menu.add(0, 1235, 0, "Update Menus");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 1234:
        	setContentView(R.layout.list_view);
            return true;
        case 1235:
        	updateMenus();
            return true;
        }
        return false;
    }
    
    private void updateMenus(){
		try{
			URL remoteFile = new URL(getResources().getString(R.string.combined_two_weeks_menus_gz_url));
			ReadableByteChannel rbc = Channels.newChannel(remoteFile.openStream());
			File localFile = new File(remoteFile.toString().substring(remoteFile.toString().lastIndexOf('/')+1));
			FileOutputStream fos = new FileOutputStream(localFile);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
			mMenus = Reader.readFile(new Scanner(Reader.uncompressFile(localFile)));
		}catch(IOException e){
			e.printStackTrace();
		}
    }
//    
//    public void switchToListView() {
//      super.onCreate(savedInstanceState);
//
//      setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, COUNTRIES));
//
//      ListView lv = getListView();
//      lv.setTextFilterEnabled(true);
//
//      lv.setOnItemClickListener(new OnItemClickListener() {
//        public void onItemClick(AdapterView<?> parent, View view,
//            int position, long id) {
//          // When clicked, show a toast with the TextView text
//          Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//              Toast.LENGTH_SHORT).show();
//        }
//      });
//    }
}