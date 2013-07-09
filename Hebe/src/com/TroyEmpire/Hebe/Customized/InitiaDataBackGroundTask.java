package com.TroyEmpire.Hebe.Customized;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.InitiateDataType;
import com.TroyEmpire.Hebe.IServices.IInitiateDataService;
import com.TroyEmpire.Hebe.Services.InitiateDataService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class InitiaDataBackGroundTask extends
		AsyncTask<InitiateDataType, Void, Void> {
	ProgressDialog progressDialog;
	private Activity activity;
	private int campusId;
	private IInitiateDataService initiateDataService = new InitiateDataService();
	private boolean netJobfeedBack;

	public InitiaDataBackGroundTask(Activity activity, int campusId) {
		this.activity = activity;
		this.campusId = campusId;
	}

	// declare other objects as per your need
	@Override
	protected void onPreExecute() {
		progressDialog = (ProgressDialog) ProgressDialog.show(activity,
				"初始化数据", "数据下载中.....", true);
	};

	@Override
	protected Void doInBackground(InitiateDataType... params) {
		if (params[0] == null)
			return null;
		switch (params[0]) {
		case 更新饭馆信息:
			netJobfeedBack = initiateDataService.initiateRestaurantData(campusId);
			break;
		case 更新地图信息:
			netJobfeedBack = initiateDataService.initiateMapData(campusId);
			break;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.setCancelable(true);
		if(netJobfeedBack == false){
		progressDialog.dismiss();
		Toast.makeText(activity, Constant.INITIATE_DATA_FAILED + " 请检查网络连接", 2000).show();
		}
		else{
			progressDialog.dismiss();
			Toast.makeText(activity, Constant.INITIATE_DATA_SUCCEED, 2000).show();
		}
	};
}