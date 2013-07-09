package com.TroyEmpire.Hebe.Customized;

import com.TroyEmpire.Hebe.Activities.XinXiPTActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class XinXiPTFirstLoadDataThread  extends AsyncTask<Void, Void, Void> {
	ProgressDialog progressDialog;
	
	private Context context;
	
	public XinXiPTFirstLoadDataThread(Context context){
		this.context = context;
	}

	// declare other objects as per your need
	@Override
	protected void onPreExecute() {
		progressDialog = (ProgressDialog) ProgressDialog.show(context,
				"请稍等", "初始化，读取信息中...",
				true);

	};

	@Override
	protected Void doInBackground(Void... params) {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	
}
