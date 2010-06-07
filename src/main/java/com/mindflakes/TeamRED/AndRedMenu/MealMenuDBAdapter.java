/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mindflakes.TeamRED.AndRedMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mindflakes.TeamRED.menuClasses.FoodItem;
import com.mindflakes.TeamRED.menuClasses.MealMenu;
import com.mindflakes.TeamRED.menuClasses.Venue;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class MealMenuDBAdapter {
	
	private static final DateTimeFormatter dtf =  DateTimeFormat.forPattern("'on' EEE, MMM dd");

    public static final String KEY_MEALMENU_NAME = "menuname";
    public static final String KEY_MEALMENU_MEALNAME = "mealname";
    public static final String KEY_MEALMENU_START = "start";
    public static final String KEY_MEALMENU_END = "end";
    public static final String KEY_MEALMENU_MOD = "mod";
    public static final String KEY_MEALMENU_STARTSTRING = "startstring";
    public static final String KEY_ROWID = "_id";
    
    public static final String KEY_VENUE_NAME = "venuename";
    public static final String KEY_VENUE_MENUROWID = "menurowid";
    
    public static final String KEY_FOODITEM_NAME = "foodname";
    //Should be 0 if not either, 1 if vegetarian, and 2 if vegan (vegan implies vegetarian)
    public static final String KEY_FOODITEM_FOOD_TYPE = "foodtype";
    public static final String KEY_FOODITEM_VENUEROWID = "venuerowid";

    private static final String TAG = "MealMenuDBAdapter";
    private DatabaseHelper mDbHelper;


    private SQLiteDatabase mDb;

    private static final String MENU_DATABASE_TABLE = "menutable";
    private static final String VENUE_DATABASE_TABLE = "venuetable";
    private static final String FOOD_DATABASE_TABLE = "foodtable";
    
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    
    /**
     * Database creation sql statement
     */
    
    private static final String MENU_DATABASE_CREATE =
        "create table "+MENU_DATABASE_TABLE+" ("+KEY_ROWID+" integer primary key autoincrement, "
        + KEY_MEALMENU_START+" integer, "
        + KEY_MEALMENU_END+" integer, "
        + KEY_MEALMENU_MOD+" integer, 	"
        + KEY_MEALMENU_NAME + " text not null, "
        + KEY_MEALMENU_STARTSTRING + " text not null, "
        + KEY_MEALMENU_MEALNAME+" text not null);";

    private static final String VENUE_DATABASE_CREATE =
        "create table "+VENUE_DATABASE_TABLE+" ("+KEY_ROWID+" integer primary key autoincrement, "
        + KEY_VENUE_NAME + " text not null, "
        + KEY_VENUE_MENUROWID+" integer not null);";
    
    private static final String FOOD_DATABASE_CREATE =
        "create table "+FOOD_DATABASE_TABLE+" ("+KEY_ROWID+" integer primary key autoincrement, "
        + KEY_FOODITEM_NAME + " text not null, "
        + KEY_FOODITEM_FOOD_TYPE+" integer not null, "
        + KEY_FOODITEM_VENUEROWID+" integer not null);";
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

//        	db.execSQL(DATABASE_CREATE);
            db.execSQL(MENU_DATABASE_CREATE);
            db.execSQL(VENUE_DATABASE_CREATE);
            db.execSQL(FOOD_DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+MENU_DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+VENUE_DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+FOOD_DATABASE_TABLE);
            onCreate(db);
        }
    }
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public MealMenuDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the menus database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public MealMenuDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database;
     */
    public void close() {
        mDbHelper.close();
    }
    
    public void clear(){
    	mDb.delete(MENU_DATABASE_TABLE, null, null);
    	mDb.delete(FOOD_DATABASE_TABLE, null, null);
    	mDb.delete(VENUE_DATABASE_TABLE, null, null);
    }


    /**
     * Create entries in the databases for the MealMenu. If the menu is
     * successfully created return the new rowId for that menu, otherwise return
     * a -1 to indicate failure.
     * 
     * @param menu MealMenu to be added to the database
     * @return rowId or -1 if failed
     */
    public long addMenu(MealMenu menu) {
//    	mDb.beginTransaction();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MEALMENU_NAME, menu.getCommonsName());
        initialValues.put(KEY_MEALMENU_MEALNAME, menu.getMealName());
        initialValues.put(KEY_MEALMENU_START, menu.getMealInterval().getStartMillis());
        initialValues.put(KEY_MEALMENU_END, menu.getMealInterval().getEndMillis());
        initialValues.put(KEY_MEALMENU_MOD, menu.getModDate().getMillis());
        initialValues.put(KEY_MEALMENU_STARTSTRING, dtf.print(menu.getMealInterval().getStart()));

        long menuID =  mDb.insert(MENU_DATABASE_TABLE, null, initialValues);
        for(Venue ven : menu.getVenues()){
        	initialValues = new ContentValues();
        	initialValues.put(KEY_VENUE_NAME,ven.getName());
        	initialValues.put(KEY_VENUE_MENUROWID, menuID);
        	long venueID=mDb.insert(VENUE_DATABASE_TABLE, null, initialValues);
        	for(FoodItem food:ven.getFoodItems()){
            	initialValues = new ContentValues();
            	initialValues.put(KEY_FOODITEM_NAME, food.getName());
            	initialValues.put(KEY_FOODITEM_FOOD_TYPE, (food.isVegetarian()?1:0)+(food.isVegan()?1:0));
            	initialValues.put(KEY_FOODITEM_VENUEROWID, venueID);
            	mDb.insert(FOOD_DATABASE_TABLE, null, initialValues);
        	}
        }
//        mDb.setTransactionSuccessful();
//    	mDb.endTransaction();
        return menuID;
    }
    
    /**
     * Adds multiple MealMenus to the database by calling addMenu on each one, and grouping 10 into each sql transaction.
     * Returns an array of the rowId's for each meal menu
     * @param menu multiple menus to be added to the database.
     * @return the rowIDs
     */
    public long[] addMenus(ArrayList<MealMenu> menu){
    	long[] result = new long[menu.size()];
    	mDb.beginTransaction();
    	for(int i = 0; i<menu.size();i++){
    		if(i%10==9){
    			mDb.setTransactionSuccessful();
    			mDb.endTransaction();
    			mDb.beginTransaction();
    		}
    		result[i]=addMenu(menu.get(i));
    	}
    	mDb.setTransactionSuccessful();
    	mDb.endTransaction();
    	return result;
    }

    /**
     * Delete the menu with the given rowId
     *  
     * @param rowId id of menu to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteMenu(long rowId) {
    	boolean result = mDb.delete(MENU_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    	if(result){
    		Cursor venCursor = mDb.query(true, VENUE_DATABASE_TABLE, new String[]{
    				KEY_ROWID}, KEY_VENUE_MENUROWID+"="+rowId,
    				null,null,null,null,null);
    		if (venCursor != null) {
    			boolean hasMoreVens =venCursor.moveToFirst();
    			while(hasMoreVens){
    				long venRowId = venCursor.getLong(venCursor.getColumnIndexOrThrow(KEY_ROWID));
    				mDb.delete(FOOD_DATABASE_TABLE, KEY_FOODITEM_VENUEROWID + "=" + venRowId, null);
    				hasMoreVens = venCursor.moveToNext();
    			}
    		}
    		mDb.delete(VENUE_DATABASE_TABLE, KEY_VENUE_MENUROWID+"="+rowId,null);
    	}
    	return result;
    }
    	/**
     * Return a Cursor over the list of all menus in the database
     * 
     * @return Cursor over all menus
     */
    public Cursor fetchAllMenuCursor() {

        return mDb.query(MENU_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MEALMENU_NAME,
        		KEY_MEALMENU_START,KEY_MEALMENU_MEALNAME},
        		null, null, null, null, null);
    }

    
    /**
     * Searches for Meals that end Between the dates given, in the Long wrapper class. Null means to not use that value.
     * Ordered by the column name given.
     * @param start start of the endMillis range
     * @param stop end of the endMillis range
     * @param orderBy what field to order the results by
     * @return a new Query for the fetch
     */
    public Cursor fetchMenusEndBetween(String commonsName,Long start, Long stop, String orderBy){
    	String selection = "";
    	if(commonsName!=null) selection = KEY_MEALMENU_NAME+"=\'"+commonsName+"\'";
    	if(start!=null) selection = addCondition(selection,KEY_MEALMENU_END+">="+start.longValue());
    	if(stop!=null) selection = addCondition(selection,KEY_MEALMENU_END+"<="+stop.longValue());
    	return mDb.query(MENU_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MEALMENU_NAME, KEY_MEALMENU_MEALNAME,
    			KEY_MEALMENU_START,KEY_MEALMENU_END,KEY_MEALMENU_MOD}, (selection.equals(""))?null:selection, null,null, null, orderBy);
    }
    
    /**
     * Method designed for the use in the FullMenuListActivity. Creates a cursor for all the menus at the 
     * specified common that end after the current time.
     * @param commonsName the commons used in the query
     * @return a new cursor with all the future meals at the specified common
     */
    public Cursor fetchMenusForMainList(String commonsName){
    	if(commonsName==null) return null;
    	return mDb.query(MENU_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MEALMENU_MEALNAME,
    			KEY_MEALMENU_STARTSTRING}, KEY_MEALMENU_NAME+"=\'"+commonsName+"\' AND "+KEY_MEALMENU_END+">="+(new DateTime().getMillis()), null,null, null, KEY_MEALMENU_START);
    }
    
    /**
     * Selects the first meal that ends after the given end time, at the common.
     * @param commonName common to search for
     * @param end end time that will be used in search, in millis
     * @param mode mode (0/1/2) representing (normal/vegetarian/vegan) food choices for the MealMenu
     * @return the first MealMenu that ends after the given date at the given commons
     */
    public MealMenu selectFirstMeal(String commonName,Long end, int mode){
    	if(commonName==null||end==null){
    		throw new NullPointerException("Parameters to selectFirstMeal cannot be null");
    	}
    	Cursor mCursor=null;
    	try{
    	mCursor= mDb.query(true, MENU_DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_MEALMENU_NAME, KEY_MEALMENU_MEALNAME, KEY_MEALMENU_START, KEY_MEALMENU_END,
                KEY_MEALMENU_MOD}, KEY_MEALMENU_NAME+"=\'"+commonName+"\' AND "+KEY_MEALMENU_END+">="+end.longValue(), null,
                null, null,KEY_MEALMENU_START, "1"); 
    	}catch(Exception e){
    		String type = e.getClass().toString();
    		System.out.println(type);
    	}
    	if(mCursor!=null && mCursor.moveToFirst()){
    		return returnNextFromCursor(mCursor,mCursor.getLong((mCursor.getColumnIndexOrThrow(KEY_ROWID))),mode);
    	}
    	return null;
    }
    
    private static String addCondition(String start, String add){
    	return ((start!=null&&!start.equals(""))?start+" AND ":"")+add;
    }
    /**
     * Return a Cursor positioned at the menu that matches the given rowId
     * 
     * @param rowId id of menu to retrieve
     * @param mode mode (0/1/2) representing (normal/vegetarian/vegan) food choices for the MealMenu
     * @return MealMenu of matching menu, if found
     * @throws SQLException if menu could not be found/retrieved
     */
    public MealMenu fetchMenu(long rowId, int mode) throws SQLException {
        Cursor mCursor =

            mDb.query(true, MENU_DATABASE_TABLE, new String[] {
                    KEY_MEALMENU_NAME, KEY_MEALMENU_MEALNAME, KEY_MEALMENU_START, KEY_MEALMENU_END,
                    KEY_MEALMENU_MOD}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
        	return returnNextFromCursor(mCursor,rowId, mode);
        } else{
        	return null;
        }        
    }
    
    private String prepareSearchSelection(String foodSearch){
    	String result = "";
    	String[] tmp = foodSearch.split(" ");
    	for(String s:tmp){
    		if(result.length()!=0){
    			result+=" AND ";
    		}
    		result+=KEY_FOODITEM_NAME+" LIKE '%"+s+"%'";
    	}
    	return result;
    }
    
    /**
     * Starts a sql query at the given dining commons, for the given foods. The foodsearch is split by spaces, and the returned cursor requires that each mealmenu has each
     * of the words in at least one food item. if commonsName == allCommons, it searches regardless of commons name.
     * @param commonsName commons to be searched at
     * @param foodSearch food string to search for
     * @param allCommons string that represents all commons
     * @return
     */
    public Cursor search(String commonsName, String foodSearch, String allCommons){
    	String selection = prepareSearchSelection(foodSearch);
    	Cursor mFoodCursor = mDb.query(true, FOOD_DATABASE_TABLE, new String[]{
        		KEY_FOODITEM_VENUEROWID}, selection,
        		null,null,null,null,null);
    	Map<Long,Boolean> map = new HashMap<Long,Boolean>();
    	if(mFoodCursor!=null&&mFoodCursor.moveToFirst()){
    		do{
    			map.put(mFoodCursor.getLong(mFoodCursor.getColumnIndexOrThrow(KEY_FOODITEM_VENUEROWID)), true);
    		}while(mFoodCursor.moveToNext());
    	} else{
    		return mDb.query(MENU_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MEALMENU_NAME,KEY_MEALMENU_MEALNAME, KEY_MEALMENU_STARTSTRING}, KEY_MEALMENU_MEALNAME+"=\'FAKENAME\'", null,null, null, KEY_MEALMENU_START);

    	}
    	Set<Long> vens = map.keySet();
    	selection = "";
    	for(Long l: vens){
    		if(selection.length()!=0){
    			selection +=" OR ";
    		}
    		selection+=KEY_ROWID+"="+l.longValue();
    	}
    	selection = selection.length()!=0?selection:KEY_MEALMENU_NAME+"=\'asdfasdfasdfasdf\'";
    	mFoodCursor = mDb.query(true, VENUE_DATABASE_TABLE, new String[]{
        		KEY_VENUE_MENUROWID}, selection,
        		null,null,null,null,null);
    	
    	map = new HashMap<Long,Boolean>();
    	if(mFoodCursor!=null&&mFoodCursor.moveToFirst()){
    		do{
    			map.put(mFoodCursor.getLong(mFoodCursor.getColumnIndexOrThrow(KEY_VENUE_MENUROWID)), true);
    		}while(mFoodCursor.moveToNext());
    	}else{
    		return mDb.query(MENU_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MEALMENU_NAME,KEY_MEALMENU_MEALNAME, KEY_MEALMENU_STARTSTRING}, KEY_MEALMENU_MEALNAME+"=\'FAKENAME\'", null,null, null, KEY_MEALMENU_START);

    	}
    	
    	vens = map.keySet();
    	selection = "";
    	for(Long l: vens){
    		if(selection.length()!=0){
    			selection +=" OR ";
    		}
    		selection+=KEY_ROWID+"="+l.longValue();
    	}
    	
    	selection = selection.length()!=0?"("+selection+")":KEY_MEALMENU_NAME+"=\'asdfasdfasdfasdf\'";
    	selection += (!commonsName.equals(allCommons))?" AND "+KEY_MEALMENU_NAME+"=\'"+commonsName+"\'":"";
    	selection += " AND "+KEY_MEALMENU_END+">="+(new DateTime()).getMillis();

    	return mDb.query(MENU_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MEALMENU_NAME,KEY_MEALMENU_MEALNAME, KEY_MEALMENU_STARTSTRING}, (selection.equals(""))?null:selection, null,null, null, KEY_MEALMENU_START);
    }
    
    private MealMenu returnNextFromCursor(Cursor mCursor, long rowId, int mode){
    	String foodType =  " AND "+KEY_FOODITEM_FOOD_TYPE+">="+ mode;
        ArrayList<Venue> vens = new ArrayList<Venue>();
        Cursor mVenCursor = mDb.query(true, VENUE_DATABASE_TABLE, new String[]{
        		KEY_ROWID,KEY_VENUE_NAME}, KEY_VENUE_MENUROWID+"="+rowId,
        		null,null,null,null,null);
        boolean hasMoreVens = mVenCursor.moveToFirst();
        while(hasMoreVens){
        	ArrayList<FoodItem> foods = new ArrayList<FoodItem>();
        	long venRowId = mVenCursor.getLong(mVenCursor.getColumnIndexOrThrow(KEY_ROWID));
        	Cursor mFoodCursor = mDb.query(true, FOOD_DATABASE_TABLE, new String[]{
            		KEY_FOODITEM_NAME,KEY_FOODITEM_FOOD_TYPE}, KEY_FOODITEM_VENUEROWID+"="+venRowId+foodType,
            		null,null,null,null,null);
        	boolean hasMoreFoods = mFoodCursor.moveToFirst();
        	while(hasMoreFoods){
        		short type = mFoodCursor.getShort(mFoodCursor.getColumnIndex(KEY_FOODITEM_FOOD_TYPE));
        		foods.add(new FoodItem(mFoodCursor.getString(mFoodCursor.getColumnIndexOrThrow(KEY_FOODITEM_NAME)),
        				(type==2)?true:false,(type>0 && type <=2)?true:false));
        		hasMoreFoods = mFoodCursor.moveToNext();
        	}
        	vens.add(new Venue(mVenCursor.getString(mVenCursor.getColumnIndexOrThrow(KEY_VENUE_NAME)), foods));
        	hasMoreVens = mVenCursor.moveToNext();
        }
        return new MealMenu(
        		mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_MEALMENU_NAME)),
        		mCursor.getLong(mCursor.getColumnIndexOrThrow(KEY_MEALMENU_START)),
        		mCursor.getLong(mCursor.getColumnIndexOrThrow(KEY_MEALMENU_END)),
        		mCursor.getLong(mCursor.getColumnIndexOrThrow(KEY_MEALMENU_MOD)),
        		vens,
        		mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_MEALMENU_MEALNAME)));
    }
}
