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
	List<HashMap<String,String>> restaurantGroups;				//������Ϣ
	List<List<Restaurant>> restaurantChilds;
	//ArrayList<ArrayList<HashMap<String, Object>>> restaurantChilds;	//��Ӧÿ�������µ�����
	ExpandableListView restaurantELV;
	private IRestaurantService restService = new RestaurantService(1, this);
	private IInitiateDataService dataService = new InitiateDataService();
	
	//�ղؼк�ȫ����λ�ú�
	private final static int FAVORITE_GROUP_POS = 0;
	private final static int ALL_GROUP_POS = 1;
	
	//�����˵�ѡ��
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
		
		//�ҵ�ExpandableListView
		restaurantELV = (ExpandableListView)findViewById(R.id.waimaixt_expandablelistview);
		//׼��һ���б���ʾ�ķ������ݣ��ֱ��С��ղؼС��͡�ȫ���͵ꡱ
		prepareData();
		//����adapter���õ����Զ����adapter
		RestaurantExpandableAdapter restaurantAdapter = new RestaurantExpandableAdapter(this);
		restaurantELV.setAdapter(restaurantAdapter);
		
		registerForContextMenu(restaurantELV);		//ע��˵�
		
		restaurantELV.expandGroup(FAVORITE_GROUP_POS);			//�Զ�չ���ղؼ�
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
	
	//���������˵���Ϣ
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		//����ֻ��������ĳ���
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			String title = ((TextView) info.targetView.findViewById(R.id.waimaixt_item_title))
					.getText().toString();
			menu.setHeaderTitle(title);
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			if(groupPos == FAVORITE_GROUP_POS){ //
				menu.add(0, MENU_DELETE_FAVORITE, 0, "���ղؼ��Ƴ�");
			}else{
				menu.add(1, MENU_ADD_FAVORITE, 0, "�����ղؼ�");
			}
			menu.add(0, MENU_CALL, 0, "���ϲ��Ŷ���");
			menu.add(0,MENU_RELOAD_REST_LIST,0,"���²͵���Ϣ");
		}
	}
	
	public void toastShow(String text)
	{
		Toast.makeText(this, text, 2000).show();
	}
	
	
	//�˵���ѡ���¼�����
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
			case MENU_ADD_FAVORITE:			//�����ղؼ�
				Log.d("add","add");
				if(selectedItem.getBookmarked() == 0){  //�ж��Ƿ��ظ�
					restService.setRestautantToBookmarked(selectedItem.getId());
					selectedItem.setBookmarked(1);
					restaurantChilds.get(FAVORITE_GROUP_POS).add(selectedItem);
					toastShow(childName + " ��ӳɹ�");
				}
				else
					toastShow(childName + " �����ղؼ�");
				restaurantELV.collapseGroup(FAVORITE_GROUP_POS);
				restaurantELV.expandGroup(FAVORITE_GROUP_POS);
				Log.d("add",selectedItem.getName());
				break;
			case MENU_DELETE_FAVORITE:		//�Ƴ��ղؼ�
				Log.d("remove","remove");
				restService.unsetRestaurantBookmarked(selectedItem.getId());
				selectedItem.setBookmarked(0);
				restaurantChilds.get(FAVORITE_GROUP_POS).remove(selectedItem);
				toastShow(childName + "�Ƴ��ɹ�");
				restaurantELV.collapseGroup(FAVORITE_GROUP_POS);
				restaurantELV.expandGroup(FAVORITE_GROUP_POS);
				Log.d("remove",selectedItem.getName());
				break;
			case MENU_CALL:					//ֱ�Ӳ�����
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
		//�ղؼ�
		HashMap<String, String> favoriteGroup = new HashMap<String, String>();
		favoriteGroup.put("group", "�ղؼ�");
		restaurantGroups.add(favoriteGroup);
		//ȫ��
		HashMap<String,String> allGroup = new HashMap<String, String>();
		allGroup.put("group", "ȫ���͵�");
		restaurantGroups.add(allGroup);
			
		//restaurantChilds����ղؼк�ȫ������ʾ�˵�
		List<Restaurant> favoriteChild = restService.getRestaurantsBookmarked();
		restaurantChilds.add(favoriteChild);
		List<Restaurant> allChild = restService.getAllRestaurants();
		restaurantChilds.add(allChild);
	}
	
	
	
	//�Զ����Adapter
	class RestaurantExpandableAdapter extends BaseExpandableListAdapter
	{
		private Context context;
		
		
		/*
		 * ���캯��:
		 * ����1:context����
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
			//ȡ��һ���͵����ʾ��Դ
			Restaurant childSelected = (Restaurant) getChild(groupPosition, childPosition);
			String itemTitle = childSelected.getName();
			String itemSubTitle = childSelected.getType() + "(���ͼۣ�" + childSelected.getMinimumOrder() + "Ԫ)"; 
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
			
			//��ȡһ���б����ļ�,������ӦԪ������
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
	

	// ʵ�ֱ�������Home��
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// ʵ�ֱ������ķ��ؼ�
	public void btnBackOnClick(View v) {
		finish();
	}

}
