package com.mindflakes.TeamRED.menuClasses;
import java.io.Serializable;
import java.util.ArrayList;

/** A container class that holds all the {@link com.mindflakes.TeamRED.menuClasses.FoodItem FoodItem}s
 * that a specific venue are serving. An example of a venue would be "Bakery" or "Grill" and all the FoodItems served there..
 * @author Johan Henkens
 *
 */
public class Venue implements Serializable{
	private String name;
	private ArrayList<FoodItem> foodItems;
	/** Constructs a <code>Venue</code> with the specified name and FoodItems to be served.
	 * @param name name of the venue at which the food is served
	 * @param foodItems ArrayList of FoodItems that will be served at this <code>Venue</code>.
	 */
	public Venue(String name, ArrayList<FoodItem> foodItems) {
		this.name = name;
		this.foodItems = (foodItems!=null)?foodItems:new ArrayList<FoodItem>();
	}
	
	public void fixFoodItems(){
		for(int i = 0; i < foodItems.size();i++){
			if(foodItems.get(i)==null) foodItems.remove(i--);
		}
		if(foodItems.size()==0) foodItems=null;
	}
	
	
	@SuppressWarnings("unused")
	private Venue() {
		
	}
	
	/** returns the name of this <code>Venue</code>. For example, "Grill" or "Bakery".
	 * @return the name of this <code>Venue</code>.
	 */
	public String getName() {
		return name;
	}
	
	
	/** returns the <code>FoodItem</code>s that are served at this <code>Venue</code>.
	 * @return the <code>FoodItem</code>s that are served at this <code>Venue</code>.
	 */
	public ArrayList<FoodItem> getFoodItems() {
		return foodItems;
	}
	/** adds the specified <code>FoodItem</code> to the existing collection of <code>FoodItem</code>s that are served at this <code>Venue</code>.
	 * @param toAdd the specified <code>FoodItem</code> to be added.
	 */
	public void addToItems(FoodItem toAdd){
		foodItems.add(toAdd);
	}
	
	/** returns a new arrayList of <code>FoodItem</code>s that are vegna.
	 * @return ArrayList of <code>FoodItem</code>s that are vegan
	 */
	public ArrayList<FoodItem> getVeganItems(){
		ArrayList<FoodItem> veganItems = new ArrayList<FoodItem>();
		if(foodItems==null) return veganItems;
		for(FoodItem food:foodItems){
			if(food.isVegan()) veganItems.add(food);
		}
		return veganItems;
	}
	
	/** returns a new arrayList of <code>FoodItem</code>s that are vegetarian.
	 * @return ArrayList of <code>FoodItem</code>s that are vegetarian
	 */
	public ArrayList<FoodItem> getVegetarianItems(){
		ArrayList<FoodItem> veganItems = new ArrayList<FoodItem>();
		for(FoodItem food:foodItems){
			if(food.isVegetarian()) veganItems.add(food);
		}
		return veganItems;
	}
	
	/** constructs an returns a new <code>Venue</code> only from the Vegan <code>FoodItem</code>s
	 * @return a new <code>Venue</code> with only Vegan foods
	 */
	public Venue newVenueFromVegan(){
		return new Venue(this.name,this.getVeganItems());
	}
	
	/** constructs an returns a new <code>Venue</code> only from the vegetarian <code>FoodItem</code>s
	 * @return a new <code>Venue</code> with only vegetarian foods
	 */
	public Venue newVenueFromVegetarian(){
		return new Venue(this.name,this.getVegetarianItems());
	}
}
