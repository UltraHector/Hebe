package com.TroyEmpire.Hebe.Activities;

import com.TroyEmpire.Hebe.Customized.XiaoYuanDTWebView;
import com.TroyEmpire.Hebe.Entities.Building;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class XiYuanDTDisplayPathActivity extends Activity {

	private XiaoYuanDTWebView xiaoYuanDTWebView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoyuandt_display_path);
		TextView tvTitleBar = (TextView) findViewById(R.id.title_text);
		tvTitleBar.setText("最短路径");
		Building sourceBuilding = (Building)getIntent().getExtras().get("sourceBuilding");
		Building destBuilding = (Building)getIntent().getExtras().get("destBuilding");
		xiaoYuanDTWebView = (XiaoYuanDTWebView) findViewById(R.id.xiaoyuandt_display_path_webview);
		xiaoYuanDTWebView.initiate(1, this);
		xiaoYuanDTWebView.setJavaScriptEnabled(true);
		xiaoYuanDTWebView.loadUrl("file:///android_asset/map.html");
		xiaoYuanDTWebView.addMarkerForPath(sourceBuilding, destBuilding);
		xiaoYuanDTWebView.disPlayShortestPath(sourceBuilding.getPathDotId(), destBuilding.getPathDotId());
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
