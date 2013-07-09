package com.TroyEmpire.Hebe.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class IScheduleDisplayActivity extends Activity{
	
	private WebView displayScheduleWebView;

	//@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ischedule_display);
		displayScheduleWebView = (WebView) findViewById(R.id.webView_displayISchedule);

	//	displayScheduleWebView.getSettings().setJavaScriptEnabled(true); // enable the java script
		displayScheduleWebView.getSettings().setSupportZoom(true);
		
		displayScheduleWebView.getSettings().setDefaultTextEncodingName("utf-8");	//set encoding name as utf-8
	    //Log.i("encoding-type", "Default Encoding = " + displayScheduleWebView.getSettings().getDefaultTextEncodingName()); 
		
		String schedulePath = getIntent().getStringExtra("schedulePath");	//get the schedule path:course or exam
		//Log.d("path",schedulePath);
		displayScheduleWebView.loadUrl(schedulePath);
	}
}
