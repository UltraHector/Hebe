package com.TroyEmpire.Hebe.Activities;

import com.TroyEmpire.Hebe.Entities.News;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class XinXiPTDisplayNewsActivity extends Activity {

	private WebView newsWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xinxipt_display_news);

		// Set the title bar text as this activity label
		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.activity_XinXiPT_label);

		newsWebView = (WebView) findViewById(R.id.xinxipt_news_display_webview);
		News news = (News) getIntent().getExtras().get("newsSelected");
		Log.d("news title", news.getTitle());
		newsWebView.loadDataWithBaseURL(null, news.getContent(), "text/html",
				"utf-8", null);

	}

	// ʵ�ֱ�������Home��
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// ʵ�ֱ������ķ��ؼ�
	public void btnBackOnClick(View v) {
		this.finish();
	}
}
