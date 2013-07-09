package com.TroyEmpire.Hebe.Services;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.DBConstant;
import com.TroyEmpire.Hebe.Entities.Meal;
import com.TroyEmpire.Hebe.Entities.Restaurant;
import com.TroyEmpire.Hebe.IServices.IRestaurantService;

public class RestaurantService implements IRestaurantService {

	// database path
	private final String hebeSDCardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constant.HEBE_STORAGE_ROOT + "/Restaurant";
	// table columns
	private final String[] restaurantColumns = new String[] {
			DBConstant.TABLE_RESTAURANT_FIELD_ID,
			DBConstant.TABLE_RESTAURANT_FIELD_NAME,
			DBConstant.TABLE_RESTAURANT_FIELD_MANAGER_NAME,
			DBConstant.TABLE_RESTAURANT_FIELD_PHONE_NUMBER,
			DBConstant.TABLE_RESTAURANT_FIELD_TRANSPORTER_NAME,
			DBConstant.TABLE_RESTAURANT_FIELD_DESCRIPTION,
			DBConstant.TABLE_RESTAURANT_FIELD_DELIVERTIME,
			DBConstant.TABLE_RESTAURANT_FIELD_BOOKMARKED,
			DBConstant.TABLE_RESTAURANT_FIELD_MINIMUM_ORDER,
			DBConstant.TABLE_RESTAURANT_FIELD_TYPE };
	private final String[] mealColumns = new String[] {
			DBConstant.TABLE_MEAL_FIELD_ID, DBConstant.TABLE_MEAL_FIELD_NAME,
			DBConstant.TABLE_MEAL_FIELD_PRICE,
			DBConstant.TABLE_MEAL_FIELD_DESCRIPTION,
			DBConstant.TABLE_MEAL_FIELD_RESTAURANT_ID };

	private int campusId;
	private String dbFile;
	private SQLiteDatabase db;
	private Activity activity;

	public RestaurantService(int campusId, Activity activity) {
		this.campusId = campusId;
		this.activity = activity;
		this.dbFile = hebeSDCardPath + "/Campus_" + campusId
				+ "_Restaurant/RestaurantDB/Restaurant.db";
		this.db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
	}

	@Override
	public int getNumberOfRestaurant() {
		// only get a single colomn is enough
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_RESTAURANT,
				new String[] { DBConstant.TABLE_RESTAURANT_FIELD_ID }, null,
				null, null, null, null);
		return cursor.getCount();
	}

	@Override
	public List<Restaurant> getRestaurantsBookmarked() {
		String condition = DBConstant.TABLE_RESTAURANT_FIELD_BOOKMARKED + "=1";
		return getRestaurants(condition);
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		String condition = null;
		return getRestaurants(condition);
	}

	@Override
	public Restaurant getRestaurantById(long id) {
		String condition = DBConstant.TABLE_RESTAURANT_FIELD_ID + "=" + id;
		return getRestaurant(condition);
	}

	@Override
	public Restaurant getRestaurantByName(String name) {
		String condition = DBConstant.TABLE_RESTAURANT_FIELD_NAME + "=" + name;
		return getRestaurant(condition);
	}

	@Override
	public List<Meal> getMealsByRestaurantId(long restId) {
		String condition = DBConstant.TABLE_MEAL_FIELD_RESTAURANT_ID + "="
				+ restId;
		return getMeals(condition);
	}

	@Override
	public List<Meal> getMealsByRestaurantName(String name) {
		long restid = getRestaurantByName(name).getId();
		String condition = DBConstant.TABLE_MEAL_FIELD_RESTAURANT_ID + "="
				+ restid;
		return getMeals(condition);
	}

	@Override
	public void setRestautantToBookmarked(long id) {
		String query = "update " + DBConstant.TABLE_RESTAURANT + " set "
				+ DBConstant.TABLE_RESTAURANT_FIELD_BOOKMARKED + "=1 where "
				+ DBConstant.TABLE_RESTAURANT_FIELD_ID + "=" + id;
		db.execSQL(query);
	}
	@Override
	public void unsetRestaurantBookmarked(long id) {
		String query = "update " + DBConstant.TABLE_RESTAURANT + " set "
				+ DBConstant.TABLE_RESTAURANT_FIELD_BOOKMARKED + "=0 where "
				+ DBConstant.TABLE_RESTAURANT_FIELD_ID + "=" + id;
		db.execSQL(query);
	}

	@SuppressWarnings("deprecation")
	private List<Meal> getMeals(String condition) {
		List<Meal> meals = new ArrayList<Meal>();
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_MEAL, mealColumns,
				condition, null, null, null, null);
		activity.startManagingCursor(cursor);
		while (cursor.moveToNext()) {
			Meal meal = new Meal();
			setMealProperties(meal, cursor);
			meals.add(meal);
		}
		return meals;

	}

	@SuppressWarnings("deprecation")
	private Restaurant getRestaurant(String condition) {
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_RESTAURANT,
				restaurantColumns, condition, null, null, null, null);
		activity.startManagingCursor(cursor);
		Restaurant restaurant = new Restaurant();
		if (cursor.moveToFirst()) {
			setRestaurantProperties(restaurant, cursor);
		}
		return restaurant;
	}

	@SuppressWarnings("deprecation")
	private List<Restaurant> getRestaurants(String condition) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_RESTAURANT,
				restaurantColumns, condition, null, null, null, null);

		activity.startManagingCursor(cursor);

		while (cursor.moveToNext()) {
			Restaurant restaurant = new Restaurant();
			setRestaurantProperties(restaurant, cursor);
			restaurants.add(restaurant);
		}
		return restaurants;
	}

	private void setRestaurantProperties(Restaurant restaurant, Cursor cursor) {
		restaurant.setBookmarked(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_BOOKMARKED)));
		restaurant
				.setDeliverTime(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_DELIVERTIME)));
		restaurant
				.setDescription(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_DESCRIPTION)));
		restaurant.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_ID)));
		restaurant
				.setManagerName(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_MANAGER_NAME)));
		restaurant
				.setMinimumOrder(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_MINIMUM_ORDER)));
		restaurant.setName(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_NAME)));
		restaurant
				.setPhoneNumber(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_PHONE_NUMBER)));
		restaurant
				.setTransporterName(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_TRANSPORTER_NAME)));
		restaurant.setType(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_RESTAURANT_FIELD_TYPE)));
	}

	private void setMealProperties(Meal meal, Cursor cursor) {
		meal.setDescription(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_MEAL_FIELD_DESCRIPTION)));
		meal.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_MEAL_FIELD_ID)));
		meal.setName(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_MEAL_FIELD_NAME)));
		meal.setPrice(cursor.getFloat(cursor
				.getColumnIndex(DBConstant.TABLE_MEAL_FIELD_PRICE)));
		meal.setRestaurantId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_MEAL_FIELD_RESTAURANT_ID)));
	}

	@Override
	public String getRestaurantLogoPath(long id) {
		return hebeSDCardPath + "/Campus_" + this.campusId
		+ "_Restaurant/RestaurantLogo/" + id + ".jpg";
	}

}
