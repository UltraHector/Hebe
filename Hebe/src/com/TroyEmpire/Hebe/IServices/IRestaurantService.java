package com.TroyEmpire.Hebe.IServices;

import java.util.List;

import com.TroyEmpire.Hebe.Entities.Meal;
import com.TroyEmpire.Hebe.Entities.Restaurant;

public interface IRestaurantService {
	public int getNumberOfRestaurant();
	public List<Restaurant> getRestaurantsBookmarked(); 
	public List<Restaurant> getAllRestaurants();
	public Restaurant getRestaurantById(long id);
	public Restaurant getRestaurantByName(String name);
	public List<Meal> getMealsByRestaurantId(long id);
	public List<Meal> getMealsByRestaurantName(String name);
	public String getRestaurantLogoPath(long id);
	public void setRestautantToBookmarked(long id);
	public void unsetRestaurantBookmarked(long id);
	
}
