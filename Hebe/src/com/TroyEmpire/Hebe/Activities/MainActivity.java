package com.TroyEmpire.Hebe.Activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.InitiateDataType;
import com.TroyEmpire.Hebe.Constant.JwcAction;
import com.TroyEmpire.Hebe.Customized.InitiaDataBackGroundTask;
import com.TroyEmpire.Hebe.Customized.UserJwcInfoDialogFragment;
import com.TroyEmpire.Hebe.IServices.IInitiateDataService;
import com.TroyEmpire.Hebe.IServices.IScheduleService;
import com.TroyEmpire.Hebe.Services.InitiateDataService;
import com.TroyEmpire.Hebe.Services.ScheduleService;
import com.TroyEmpire.Hebe.Util.HebeCommons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {

	private GridView gridview;
	private IInitiateDataService initDataService = new InitiateDataService();
	IScheduleService scheduleService = new ScheduleService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (checkIfNecessaryToDownloadData(1))
			startProgress();

		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("imageItem", Constant.FUNCTION_LOGOS[i]);
			item.put("textItem", Constant.FUNCTION_NAMES[i]);
			items.add(item);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.maingridview_item, new String[] { "imageItem",
						"textItem" }, new int[] { R.id.function_image_item,
						R.id.function_name });
		gridview = (GridView) findViewById(R.id.main_gridview);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(MainActivity.this,
							XinXiPTActivity.class));
					break;
				case 1:
					startActivity(new Intent(MainActivity.this,
							XiaoYuanDTActivity.class));
					break;
				case 2:
					startActivity(new Intent(MainActivity.this,
							IScheduleActivity.class));
					break;
				case 3:
					startActivity(new Intent(MainActivity.this,
							WaiMaiXTActivity.class));
					break;
				case 4:
					startActivity(new Intent(MainActivity.this,
							MainFunction5.class));
					break;
				case 5:
					startActivity(new Intent(MainActivity.this,
							InternalAccessLibraryWebViewActivity.class));
					break;
				}
			}
		});
	}

	public void btnSettingOnClick(View v) {
		startActivity(new Intent(this, PrefsActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		UserJwcInfoDialogFragment userInfoDialog;
		InitiaDataBackGroundTask initiaDataBackGroundTask = new InitiaDataBackGroundTask(
				this, 1);
		switch (item.getItemId()) {
		case R.id.update_for_map:
			initiaDataBackGroundTask.execute(InitiateDataType.更新地图信息);
			break;
		case R.id.update_for_exam_schedule:
			userInfoDialog = new UserJwcInfoDialogFragment(MainActivity.this,
					JwcAction.UPDATE_EXAM_SCHEDULE, scheduleService);
			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
			break;
		case R.id.update_for_course_schedule:
			userInfoDialog = new UserJwcInfoDialogFragment(MainActivity.this,
					JwcAction.UPDATE_COURSE_SCHEDULE, scheduleService);
			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
			break;
		case R.id.update_for_waimaixt:
			initiaDataBackGroundTask.execute(InitiateDataType.更新饭馆信息);
			break;
		}

		return false;
	}

	/**
	 * @param campusId
	 * @return if the db file not exists, create folder structure and return
	 *         true
	 */
	private boolean checkIfNecessaryToDownloadData(int campusId) {
		boolean yesOrNo = false;
		String dbRestFolderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ Constant.HEBE_STORAGE_ROOT
				+ "/Restaurant/Campus_" + campusId + "_Restaurant/RestaurantDB";
		String logoRestFolderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ Constant.HEBE_STORAGE_ROOT
				+ "/Restaurant/Campus_"
				+ campusId
				+ "_Restaurant/RestaurantLogo";
		String dbMapFolderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ Constant.HEBE_STORAGE_ROOT
				+ "/Map/Campus_" + campusId + "_Map/MapDB";
		String imageMapFolderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ Constant.HEBE_STORAGE_ROOT
				+ "/Map/Campus_" + campusId + "_Map/MapImage";

		File dbRestFolderFile = new File(dbRestFolderPath);
		File dbMapFolderFile = new File(dbMapFolderPath);
		File imageMapFolderFile = new File(imageMapFolderPath);
		File logoRestFolderFile = new File(logoRestFolderPath);

		if (!dbRestFolderFile.exists()) {
			dbRestFolderFile.mkdirs();
			yesOrNo = true;
		}
		if (!logoRestFolderFile.exists()) {
			logoRestFolderFile.mkdirs();
			yesOrNo = true;
		}
		if (!dbMapFolderFile.exists()) {
			dbMapFolderFile.mkdirs();
			yesOrNo = true;
		}
		if (!imageMapFolderFile.exists()) {
			imageMapFolderFile.mkdirs();
			yesOrNo = true;
		}
		return yesOrNo;
	}
	
	private void copyHebeDataFromAssertToSDCard(int campusId){
		try {
			String dbRestPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ Constant.HEBE_STORAGE_ROOT
					+ "/Restaurant/Campus_" + campusId + "_Restaurant/RestaurantDB/" + Constant.RESTAURANT_DB_FILE_NAME;
			String dbMapPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ Constant.HEBE_STORAGE_ROOT
					+ "/Map/Campus_" + campusId + "_Map/MapDB/" + Constant.MAP_DB_FILE_NAME;
			File restFile = new File(dbRestPath);
			restFile.createNewFile();
			File mapFile = new File(dbMapPath);
			mapFile.createNewFile();
			
			InputStream in_rest = getAssets().open(
					Constant.HEBE_CLIENT_DB_DATA + "/Restaurant.db");
			InputStream in_map = getAssets().open(
					Constant.HEBE_CLIENT_DB_DATA + "/Map.db");
			
			OutputStream out_rest = new FileOutputStream(restFile);
			OutputStream out_map = new FileOutputStream(mapFile);
			IOUtils.copy(in_rest, out_rest);
			IOUtils.copy(in_map, out_map);
		} catch (IOException e) {
		}
	}

	public class LoadData extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		boolean whetherConnectToServer = false;

		@Override
		protected void onPreExecute() {
			progressDialog = (ProgressDialog) ProgressDialog.show(
					MainActivity.this, "下载数据包", "数据初始化中.....", true);
		};

		@Override
		protected Void doInBackground(Void... params) {
			if ((whetherConnectToServer = HebeCommons.checkWhetherCouldConnectToServer())) {
				initDataService.initiateMapData(1);
				initDataService.initiateRestaurantData(1);
			} else {
				copyHebeDataFromAssertToSDCard(1);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(whetherConnectToServer)
				Toast.makeText(MainActivity.this,
						"初始化数据成功", 1000).show();
			else
				Toast.makeText(MainActivity.this,
					"网络连接失败，初始化数据使用内置数据库。请及时手工更新", 2500).show();
		};
	}

	public void startProgress() {
		LoadData task = new LoadData();
		task.execute();
	}
}
