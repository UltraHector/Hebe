package com.TroyEmpire.Hebe.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.TroyEmpire.Hebe.Entities.Meal;
import com.TroyEmpire.Hebe.Entities.Restaurant;
import com.TroyEmpire.Hebe.IServices.IRestaurantService;
import com.TroyEmpire.Hebe.Services.RestaurantService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WaiMaiXTRestaurantActivity extends Activity {

	private IRestaurantService restService = new RestaurantService(1,this);
	private Restaurant restaurant;
	private String restaurantPhoneNumber;
	private String restaurantIntroduction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waimaixt_restaurant);

		TextView titleText = (TextView) findViewById(R.id.title_text);
		Long restaurantId = getIntent().getLongExtra("restaurantId", 0);
		//Log.d("restID",restaurantId.toString());
		restaurant = restService.getRestaurantById(restaurantId);
		titleText.setText(restaurant.getName());
		
		restaurantPhoneNumber = restaurant.getPhoneNumber();
		restaurantIntroduction = restaurant.getDescription() + "\n电话：" + restaurantPhoneNumber + 
				"\t\t起送价：" + restaurant.getMinimumOrder() + "元";
		TextView restaurantIntroductionTextView = (TextView) findViewById(R.id.waimaixt_restaurant_introduction_textview);
		restaurantIntroductionTextView.setText(restaurantIntroduction);
		
		//绑定Layout里面的ListView  
        ListView list = (ListView) findViewById(R.id.waimaixt_menus_listview);  
          
        //生成动态数组，加入数据  
        List<Meal> meals = restService.getMealsByRestaurantId(restaurantId);
        List<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
        for(Meal tmp:meals)  
        {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("mealName", tmp.getName());
            map.put("mealPrice", "￥"+ tmp.getPrice()+"元/份");
            listItem.add(map);  
        }  
        //生成适配器的Item和动态数组对应的元素  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
            R.layout.waimaixt_menu_item,//ListItem的XML实现  
            //动态数组与ImageItem对应的子项          
            new String[] {"mealName","mealPrice"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.waimaixt_meal_item_name_textview,R.id.waimaixt_meal_item_price_textview}  
        );  
         
        //添加并且显示  
        list.setAdapter(listItemAdapter);  
	}
	
	// 一键订餐按钮事件的实现
	public void btnCallForMealEvent(View v){
		Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + restaurantPhoneNumber));
		startActivity(callIntent);
	}

	// 实现标题栏的Home键
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// 实现标题栏的返回键
	public void btnBackOnClick(View v) {
		finish();
	}
}
