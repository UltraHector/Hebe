package com.TroyEmpire.Hebe.Activities;

/**
 * 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.TroyEmpire.Hebe.Entities.Restaurant;
import com.TroyEmpire.Hebe.IServices.IInitiateDataService;
import com.TroyEmpire.Hebe.IServices.IRestaurantService;
import com.TroyEmpire.Hebe.Services.InitiateDataService;
import com.TroyEmpire.Hebe.Services.RestaurantService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Hector
 * 
 */
public class WaiMaiXTActivity extends Activity 
{
	List<HashMap<String,String>> restaurantGroups;				//分类信息
	List<List<Restaurant>> restaurantChilds;
	//ArrayList<ArrayList<HashMap<String, Object>>> restaurantChilds;	//对应每个分类下的子项
	ExpandableListView restaurantELV;
	private IRestaurantService restService = new RestaurantService(1, this);
	private IInitiateDataService dataService = new InitiateDataService();
	
	//收藏夹和全部的位置号
	private final static int FAVORITE_GROUP_POS = 0;
	private final static int ALL_GROUP_POS = 1;
	
	//长按菜单选项
	private final static int MENU_ADD_FAVORITE = 0;
	private final static int MENU_DELETE_FAVORITE = 1;
	private final static int MENU_CALL = 2;
	private final static int MENU_RELOAD_REST_LIST = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waimaixt);

		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.activity_WaiMaiXT_label);
		
		//找到ExpandableListView
		restaurantELV = (ExpandableListView)findViewById(R.id.waimaixt_expandablelistview);
		//准备一级列表显示的分组数据，分别有“收藏夹”和“全部餐店”
		prepareData();
		//配置adapter，用的是自定义的adapter
		RestaurantExpandableAdapter restaurantAdapter = new RestaurantExpandableAdapter(this);
		restaurantELV.setAdapter(restaurantAdapter);
		
		registerForContextMenu(restaurantELV);		//注册菜单
		
		restaurantELV.expandGroup(FAVORITE_GROUP_POS);			//自动展开收藏夹
		restaurantELV.setOnChildClickListener(new OnChildClickListener() {  
            @Override  
            public boolean onChildClick(ExpandableListView elv, View v, 
            		int groupPosition, int childPosition, long id) {  
 
            	Intent restaurantIntent = new Intent(WaiMaiXTActivity.this,WaiMaiXTRestaurantActivity.class);
            	Long restaurantId = ((Restaurant)(elv.getExpandableListAdapter().
            			getChild(groupPosition, childPosition))).getId();
            	restaurantIntent.putExtra("restaurantId", restaurantId);
            	startActivity(restaurantIntent); 
                return false;  
            }  
        });  
	}
	
	//创建长按菜单信息
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		//这里只处理子项的长按
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			String title = ((TextView) info.targetView.findViewById(R.id.waimaixt_item_title))
					.getText().toString();
			menu.setHeaderTitle(title);
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			if(groupPos == FAVORITE_GROUP_POS){ //
				menu.add(0, MENU_DELETE_FAVORITE, 0, "从收藏夹移除");
			}else{
				menu.add(1, MENU_ADD_FAVORITE, 0, "加入收藏夹");
			}
			menu.add(0, MENU_CALL, 0, "马上拨号订餐");
			menu.add(0,MENU_RELOAD_REST_LIST,0,"更新餐店信息");
		}
	}
	
	public void toastShow(String text)
	{
		Toast.makeText(this, text, 2000).show();
	}
	
	
	//菜单项选择事件处理
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		ExpandableListContextMenuInfo menuInfo = (ExpandableListContextMenuInfo)item.getMenuInfo();
		int type = ExpandableListView.getPackedPositionType(menuInfo.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			Log.i("type","child");
		}else
			Log.i("type", "group");
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			String childName = ((TextView)menuInfo.targetView.findViewById(R.id.waimaixt_item_title)).getText().toString();
			int groupPos = ExpandableListView.getPackedPositionGroup(menuInfo.packedPosition); 
	        int childPos = ExpandableListView.getPackedPositionChild(menuInfo.packedPosition);
	        Restaurant selectedItem = restaurantChilds.get(groupPos).get(childPos);
			switch(item.getItemId()){
			case MENU_ADD_FAVORITE:			//加入收藏夹
				Log.d("add","add");
				if(selectedItem.getBookmarked() == 0){  //判断是否重复
					restService.setRestautantToBookmarked(selectedItem.getId());
					selectedItem.setBookmarked(1);
					restaurantChilds.get(FAVORITE_GROUP_POS).add(selectedItem);
					toastShow(childName + " 添加成功");
				}
				else
					toastShow(childName + " 已在收藏夹");
				restaurantELV.collapseGroup(FAVORITE_GROUP_POS);
				restaurantELV.expandGroup(FAVORITE_GROUP_POS);
				Log.d("add",selectedItem.getName());
				break;
			case MENU_DELETE_FAVORITE:		//移出收藏夹
				Log.d("remove","remove");
				restService.unsetRestaurantBookmarked(selectedItem.getId());
				selectedItem.setBookmarked(0);
				restaurantChilds.get(FAVORITE_GROUP_POS).remove(selectedItem);
				toastShow(childName + "移除成功");
				restaurantELV.collapseGroup(FAVORITE_GROUP_POS);
				restaurantELV.expandGroup(FAVORITE_GROUP_POS);
				Log.d("remove",selectedItem.getName());
				break;
			case MENU_CALL:					//直接拨订餐
				Log.d("call","call");
				Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + selectedItem.getPhoneNumber()));
				startActivity(callIntent);
				break;
			case MENU_RELOAD_REST_LIST:
				Log.d("reload","restaurant list");
				dataService.initiateRestaurantData(1);
			}
		}
		return true;
	}
	
	void prepareData()
	{
		restaurantGroups = new ArrayList<HashMap<String,String>>();
		restaurantChilds = new ArrayList<List<Restaurant>>();
		//收藏夹
		HashMap<String, String> favoriteGroup = new HashMap<String, String>();
		favoriteGroup.put("group", "收藏夹");
		restaurantGroups.add(favoriteGroup);
		//全部
		HashMap<String,String> allGroup = new HashMap<String, String>();
		allGroup.put("group", "全部餐店");
		restaurantGroups.add(allGroup);
			
		//restaurantChilds存放收藏夹和全部的显示菜单
		List<Restaurant> favoriteChild = restService.getRestaurantsBookmarked();
		restaurantChilds.add(favoriteChild);
		List<Restaurant> allChild = restService.getAllRestaurants();
		restaurantChilds.add(allChild);
	}
	
	
	
	//自定义的Adapter
	class RestaurantExpandableAdapter extends BaseExpandableListAdapter
	{
		private Context context;
		
		
		/*
		 * 构造函数:
		 * 参数1:context对象
		 */
		public RestaurantExpandableAdapter(Context context){
			this.context = context;
		}


		@Override
		public Object getChild(int groupPosition, int childPosition) 
		{
			return restaurantChilds.get(groupPosition).get(childPosition);
		}


		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}


		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) 
		{
			//取出一个餐店的显示资源
			Restaurant childSelected = (Restaurant) getChild(groupPosition, childPosition);
			String itemTitle = childSelected.getName();
			String itemSubTitle = childSelected.getType() + "(起送价：" + childSelected.getMinimumOrder() + "元)"; 
			String itemImagePath = restService.getRestaurantLogoPath(childSelected.getId());
			Log.d("rest logo uri", itemImagePath);
			
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.waimaixt_child, null);
			
			ImageView imageView = (ImageView)relativeLayout.findViewById(R.id.waimaixt_item_image);
			File temp = new File(itemImagePath);
			if(temp.exists()){
				imageView.setImageBitmap(BitmapFactory.decodeFile(itemImagePath));
			}
			else{
				imageView.setImageResource(R.drawable.restaurant_default_logo);
			}
			
			TextView titleTextView = (TextView) relativeLayout.findViewById(R.id.waimaixt_item_title);
			titleTextView.setText(itemTitle);
			TextView subTitleTextView = (TextView) relativeLayout.findViewById(R.id.waimaixt_item_subtitle);
			subTitleTextView.setText(itemSubTitle);
			
			return relativeLayout;
		}


		@Override
		public int getChildrenCount(int groupPosition) 
		{
			return restaurantChilds.get(groupPosition).size();
		}


		@Override
		public Object getGroup(int groupPosition)
		{
			return restaurantGroups.get(groupPosition);
		}


		@Override
		public int getGroupCount() 
		{
			return restaurantGroups.size();
		}


		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}


		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) 
		{
			String text = restaurantGroups.get(groupPosition).get("group");
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			//获取一级列表布局文件,设置相应元素属性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.waimaixt_group, null);
			TextView textView = (TextView)linearLayout.findViewById(R.id.waimaixt_group_textview);
			textView.setText(text + "(" + this.getChildrenCount(groupPosition) + ")");
			
			return linearLayout;
		}


		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		
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
