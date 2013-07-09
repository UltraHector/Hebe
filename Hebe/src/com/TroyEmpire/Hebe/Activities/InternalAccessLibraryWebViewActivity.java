package com.TroyEmpire.Hebe.Activities;


import com.TroyEmpire.Hebe.Constant.Constant;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InternalAccessLibraryWebViewActivity extends Activity {

	private WebView libWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ô���ģʽ����Ϊ��Ҫ��ʾ�������ڱ����� 
		requestWindowFeature(Window.FEATURE_PROGRESS);  
        setProgressBarVisibility(true); 
		setContentView(R.layout.activity_internal_access_library_web_view);

		libWebView = (WebView) findViewById(R.id.webView);

		//libWebView.getSettings().setJavaScriptEnabled(true); // enable the java script
		libWebView.getSettings().setSupportZoom(true);

		libWebView.setWebViewClient(new WebViewClient()); // set a webview
															// client to handle
															// the request
		libWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activity��Webview���ݼ��س̶Ⱦ����������Ľ��ȴ�С
				// �����ص�100%��ʱ�� �������Զ���ʧ
				InternalAccessLibraryWebViewActivity.this.setProgress(progress * 100);
			}
		});
		String url = Constant.LibraryUrl;
		libWebView.loadUrl(url);

	}

	// ���ؼ��Ĵ���
	public boolean onKeyDown(int keyCoder, KeyEvent event) {

		if (libWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			libWebView.goBack(); 	// goBack()��ʾ����webView����һҳ��
			return true;
		}
		return super.onKeyDown(keyCoder, event); 	// û�к���ʱ������main activity
	}

}
