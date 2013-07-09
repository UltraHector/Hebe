package com.TroyEmpire.Hebe.Activities;

import com.TroyEmpire.Hebe.Constant.Constant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class MainFunction5 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_function_5);
		
		// Set the title bar text as this activity label
		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.main_function_5_label);
		
		WebView showText = (WebView) findViewById(R.id.mainfunction_5_display_webview);
		showText.getSettings().setJavaScriptEnabled(true);
		showText.loadUrl(Constant.HEBE_MANUAL_HTML_PATH);
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