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
		restaurantIntroduction = restaurant.getDescription() + "\n�绰��" + restaurantPhoneNumber + 
				"\t\t���ͼۣ�" + restaurant.getMinimumOrder() + "Ԫ";
		TextView restaurantIntroductionTextView = (TextView) findViewById(R.id.waimaixt_restaurant_introduction_textview);
		restaurantIntroductionTextView.setText(restaurantIntroduction);
		
		//��Layout�����ListView  
        ListView list = (ListView) findViewById(R.id.waimaixt_menus_listview);  
          
        //���ɶ�̬���飬��������  
        List<Meal> meals = restService.getMealsByRestaurantId(restaurantId);
        List<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
        for(Meal tmp:meals)  
        {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("mealName", tmp.getName());
            map.put("mealPrice", "��"+ tmp.getPrice()+"Ԫ/��");
            listItem.add(map);  
        }  
        //������������Item�Ͷ�̬�����Ӧ��Ԫ��  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//����Դ   
            R.layout.waimaixt_menu_item,//ListItem��XMLʵ��  
            //��̬������ImageItem��Ӧ������          
            new String[] {"mealName","mealPrice"},   
            //ImageItem��XML�ļ������һ��ImageView,����TextView ID  
            new int[] {R.id.waimaixt_meal_item_name_textview,R.id.waimaixt_meal_item_price_textview}  
        );  
         
        //��Ӳ�����ʾ  
        list.setAdapter(listItemAdapter);  
	}
	
	// һ�����Ͱ�ť�¼���ʵ��
	public void btnCallForMealEvent(View v){
		Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + restaurantPhoneNumber));
		startActivity(callIntent);
	}

	// ʵ�ֱ�������Home��
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// ʵ�ֱ������ķ��ؼ�
	public void btnBackOnClick(View v) {
		finish();
	}
}
