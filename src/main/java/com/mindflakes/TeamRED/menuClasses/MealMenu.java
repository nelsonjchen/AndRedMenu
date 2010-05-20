package com.mindflakes.TeamRED.menuClasses;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import org.joda.time.format.DateTimeFormat;



import java.io.Serializable;
import java.util.ArrayList;

/** A container object that represents a specific meal at a specific dining common.
 * @author Johan Henkens
 *
 */
public class MealMenu implements Serializable{
	private String _menuKey;
	private String commonsName;
	private long startMillis, endMillis, modMillis;
	private ArrayList<Venue> venues;
	private String mealName;

	
	/** constructs a MealMenu object with the specified parameters. Time is passed to the constructor in the form returned by
	 * a call to Joda Time's {@link org.joda.time.DateTime#getMillis() DateTime.getMillis. This convention is used for the start, end, and modification times
	 * @param commonsName Name of the dinning common at which this meal occurs. Should not contain 'Commons' at the end.
	 * @param startMillis number of milliseconds after the java epoch at which this meal starts
	 * @param endMillis number of milliseconds after the java epoch at which this meal end
	 * @param modMillis number of milliseconds after the java epoch at which this MealMenu was modified. Mostly used with a parser in
	 * which the modification date depends on when the input file was created.
	 * @param venues arrayList of Venues that will be serving food at this meal.
	 * @param mealName name of the meal being served (for example, "Breakfast").
	 */
	public MealMenu(String commonsName, long startMillis, long endMillis,
			long modMillis, ArrayList<Venue> venues, String mealName) {
		this.commonsName = commonsName;
		this.startMillis = startMillis;
		this.endMillis = endMillis;
		this.modMillis = modMillis;
		this.venues = venues;
		this.mealName = mealName;
		setMenuKey();
		removeEmptyVenues();
	}
	
	private void removeEmptyVenues(){
		for(int i = 0; i<venues.size();i++){
			if(venues.get(i)==null){
				venues.remove(i);
				i--;
			} else if(venues.get(i).getFoodItems()==null){
				venues.remove(i);
				i--;
			}else if(venues.get(i).getFoodItems().size()==0){
				venues.remove(i);
				i--;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private MealMenu(){
		this.commonsName = null;
		this.venues = new ArrayList<Venue>();
		this.mealName = null;
		this.modMillis = 0;
		this.startMillis = 0;
		this.endMillis = 0;
		
	}
	
	public String getMenuKey(){
		return _menuKey;
	}
	
	/**
	 * This method sets a key for unique persistance.
	 */
	private void setMenuKey() {
		 this._menuKey = this.commonsName.toLowerCase() +
		 DateTimeFormat.forPattern("MMddyyyyHHmm")
		 .print(startMillis);
	}
	
	/** gets the name of the dining commons at which this meal occurs. For example, "Carrillo" or "Ortega".
	 * @return name of the dining commons at which this meal occurs
	 */
	public String getCommonsName() {
		return commonsName;
	}
	/** retrieves all the venues which will be serving food at this meal
	 * @return the venues which will be serving food at this meal.
	 */
	public ArrayList<Venue> getVenues() {
		return venues;
	}
	
	/**	gets the name of this meal. For example, "Dinner", "Breakfast" or "Late Night"
	 * @return name of this meal
	 */
	public String getMealName() {
		return mealName;
	}
	
	/**	creates and return an <code>Interval</code> object from the start and end values of this meal. The <code>Interval</code> represents the time 
	 * during which the meal is being served.
	 * @return interval the <code>Interval</code> representing the time during which the meal is served
	 */
	public Interval getMealInterval(){
		return new Interval(startMillis, endMillis,DateTimeZone.forID("America/Los_Angeles"));
	}
	
	/** creates and returns a <code>DateTime</code> object from the modTime of this meal. 
	 * The <code>DateTime</code> is the time at which this <code>MealMenu</code> object's information was last modified.
	 * @return the DateTime at which this MealMenu was last modified.
	 */
	public DateTime getModDate() {
		return new DateTime(modMillis,DateTimeZone.forID("America/Los_Angeles"));
	}
	
	/**	sets the modDate of this <code>MealMenu</code> to the specified modDate
	 * @param modDate <code>DateTime</code> containing the modDate that will be used
	 */
	public void setModDate(DateTime modDate) {
		this.modMillis = modDate.getMillis();
	}
	
	/** sets the modDate of this <code>MealMenu</code> to the specified modDate
	 * @param modMilis time represented by the milliseonds since the epoch of java to use as the new modification date
	 */
	public void setModDate(long modMillis){
		this.modMillis=modMillis;
	}
	
	/** constructs a new arrayList holding all the venues with vegan food items.
	 * @return a new arraylist holding only venues with vegan food items
	 */
	public ArrayList<Venue> getVeganVenues(){
		ArrayList<Venue> veganVenues = new ArrayList<Venue>();
		for(Venue venue:venues){
			if(venue.getVeganItems().size()>0){
				veganVenues.add(venue.newVenueFromVegan());
			}
		}
		return veganVenues;
	}
	/** constructs a new arrayList holding all the venues with vegetarian food items.
	 * @return a new arraylist holding only venues with vegetarian food items
	 */
	public ArrayList<Venue> getVegetarianVenues(){
		ArrayList<Venue> vgtVenues = new ArrayList<Venue>();
		for(Venue venue:venues){
			if(venue.getVegetarianItems().size()>0){
				vgtVenues.add(venue.newVenueFromVegetarian());
			}
		}
		return vgtVenues;
	}
	
	/** constructs a new MealMenu instance that is a copy of this, but with only the Vegan foods in the venues
	 * @return a new MealMenu object with only Vegan foods
	 */
	public MealMenu newMealMenuFromVegan(){
		return new MealMenu(this.commonsName,this.startMillis,this.endMillis,this.modMillis,this.getVeganVenues(),this.mealName);
	}
	
	/** constructs a new MealMenu instance that is a copy of this, but with only the vegetarian foods in the venues
	 * @return a new MealMenu object with only vegetarian foods
	 */
	public MealMenu newMealMenuFromVegetarian(){
		return new MealMenu(this.commonsName,this.startMillis,this.endMillis,this.modMillis,this.getVegetarianVenues(),this.mealName);
	}

}
