package com.mindflakes.TeamRED.menuClasses;

import java.io.Serializable;

/**
 * The class FoodItem is used to hold information about a single item of foods
 * @author Johan Henkens
 *
 */
public class FoodItem implements Serializable{
	private String name;
	private boolean vegan;
	private boolean vegetarian;
	
	
	/**
	 * Constructs a new <code>FoodItem</code> object from the given name and values for vegan and vegetarian.s
	 * @param name Name of the food this <code>FoodItem</code> object represents.
	 * @param vegan <code>Boolean</code> value representing whether of not the <code>FoodItem</code> is vegan.
	 * @param vegetarian <code>Boolean</code> value representing whether or not the FoodItem is vegetarian.
	 */
	public FoodItem(String name, boolean vegan, boolean vegetarian) {
		this.name = name;
		this.vegan = vegan;
		this.vegetarian = vegetarian;
		if(name==null) name = "NULLNAME";
	}
	
	@SuppressWarnings("unused")
	private FoodItem() {
		
	}
	
	
	/** Returns the name of the food
	 * @return the name of the food
	 */
	public String getName() {
		return name;
	}
	
	
	/** Returns <code>true</code> if the food is vegan.
	 * @return <code>true</code> if the food is vegan; <code>false</code> otherwise.
	 */
	public boolean isVegan() {
		return vegan;
	}
	/** Returns <code>true</code> if the food is vegetarian
	 * @return <code>true</code> if the food is vegetarian; <code>false</code> otherwise.
	 */
	public boolean isVegetarian() {
		return vegetarian;
	}
}
