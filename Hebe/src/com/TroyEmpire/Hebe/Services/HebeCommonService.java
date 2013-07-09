package com.TroyEmpire.Hebe.Services;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.TroyEmpire.Hebe.IServices.IHebeCommonService;

public class HebeCommonService implements IHebeCommonService{
	
	private boolean yesOrNo;

	@Override
	public boolean whetherCanConnectToJwc(final String url) {
		Thread child = new Thread(){
			@Override
			public void run(){
				try {
					Jsoup.connect(url).execute();
					yesOrNo = true;
				} catch (IOException e) {
					yesOrNo = false;
				}
			}
		};
		child.start();
		try {
			child.join();
		} catch (Exception e) {
			return false;
		}	
		return yesOrNo;
	}
}
