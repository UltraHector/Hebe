package com.TroyEmpire.Hebe.Activities;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Customized.XiaoYuanDTWebView;
import com.TroyEmpire.Hebe.Entities.Building;
import com.TroyEmpire.Hebe.IServices.IBuildingService;
import com.TroyEmpire.Hebe.IServices.IGpsService;
import com.TroyEmpire.Hebe.IServices.IMapService;
import com.TroyEmpire.Hebe.Services.BuildingService;
import com.TroyEmpire.Hebe.Services.GpsService;
import com.TroyEmpire.Hebe.Services.MapService;
import com.TroyEmpire.Hebe.Util.Checker;
import com.TroyEmpire.Hebe.Util.Checker.Resource;

import android.location.LocationListener;
import android.location.LocationManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class XiaoYuanDTActivity extends Activity implements OnClickListener {

	private XiaoYuanDTWebView xiaoYuanDTWebView;
	private IMapService mapService;
	private IBuildingService buildingService;
	private IGpsService gpsService;
	private LocationManager locationManager;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoyuandt);
		
		TextView tvTitleBar = (TextView) findViewById(R.id.title_text);
		tvTitleBar.setText("校园地图");

		ImageButton btnLocation = (ImageButton) findViewById(R.id.xiaoyuandt_location_btn);
		btnLocation.setOnClickListener(this);
		Button btnPath = (Button) findViewById(R.id.xiaoyuandt_path_btn);
		btnPath.setOnClickListener(this);
		Button btnSearch = (Button) findViewById(R.id.xiaoyuandt_search_btn);
		btnSearch.setOnClickListener(this);
		
		xiaoYuanDTWebView = (XiaoYuanDTWebView) findViewById(R.id.xiaoyuandt_webview);
		xiaoYuanDTWebView.initiate(1, this);
		xiaoYuanDTWebView.setJavaScriptEnabled(true);
		xiaoYuanDTWebView.loadUrl("file:///android_asset/map.html");
		// 先有xiaoYuanDTWebView 才有MapService
		mapService = new MapService(this);
		buildingService = new BuildingService(1, this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gpsService = new GpsService(locationManager);
	}

	// 实现标题栏的Home键
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// 实现标题栏的返回键
	public void btnBackOnClick(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xiaoyuandt_location_btn: {
			double lati = gpsLatitudeTransform(gpsService.getCurrentLatitute());
			double longi = gpsLongiTransform(gpsService.getCurrentLongitude());
			Building building = buildingService.getBuildingByLocation(lati,
					longi);
			if(building.getLatitude() == 0)
				
			xiaoYuanDTWebView.addMarkerForSearch(building);
			break;
		}
		case R.id.xiaoyuandt_path_btn:
			Intent pathIntent = new Intent(this, XiaoYuanDTPathActivity.class);
			startActivity(pathIntent);
			break;
		case R.id.xiaoyuandt_search_btn:
			Intent searchIntent = new Intent(this,
					XiaoYuanDTSearchActivity.class);
			startActivity(searchIntent);
			break;
		}

	}
	

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		new Checker(this).pass(new Checker.Pass() {
		     @Override public void pass() {
		    	 locationManager.requestLocationUpdates(gpsService.getProvider(), 400,
		 				1, (LocationListener) gpsService);
		     }
		  }).check(Resource.GPS, Resource.NETWORK);

	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates((LocationListener) gpsService);
	}

	private double gpsLatitudeTransform(double value) {
		double newValue = value * Constant.GPS_RATIO
				- Constant.BASE_POINT_LATITUDE;
		return newValue;
	}

	private double gpsLongiTransform(double value) {
		double newValue = value * Constant.GPS_RATIO
				- Constant.BASE_POINT_LONGITUDE;
		return newValue;
	}
}
