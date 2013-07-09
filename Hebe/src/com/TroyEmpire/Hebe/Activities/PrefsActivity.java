package com.TroyEmpire.Hebe.Activities;

import com.TroyEmpire.Hebe.Constant.JwcAction;
import com.TroyEmpire.Hebe.Customized.UserJwcInfoDialogFragment;
import com.TroyEmpire.Hebe.IServices.IInitiateDataService;
import com.TroyEmpire.Hebe.IServices.IRestaurantService;
import com.TroyEmpire.Hebe.IServices.IScheduleService;
import com.TroyEmpire.Hebe.Services.InitiateDataService;
import com.TroyEmpire.Hebe.Services.RestaurantService;
import com.TroyEmpire.Hebe.Services.ScheduleService;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

// Handler for the activity of preference settings
public class PrefsActivity extends FragmentActivity implements OnClickListener {

	private IScheduleService scheduleService = new ScheduleService(this);
	private IInitiateDataService restaurantService = new InitiateDataService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		TextView enableSmartViberateTextView = (TextView) findViewById(R.id.id_enable_smart_viberate_textview);
		TextView enableWifiTextView = (TextView) findViewById(R.id.id_enable_wifi_textview);
		TextView updateCourseScheduleTextView = (TextView) findViewById(R.id.id_update_course_schedule);
		TextView updateExamScheduleTextView = (TextView) findViewById(R.id.id_update_exam_schedule);
		TextView updateMapDataTextView = (TextView) findViewById(R.id.id_update_map_data);
		TextView updateRestaurantDataTextView = (TextView) findViewById(R.id.id_update_restaurant_data);
		CheckBox enableSmartViberateBox = (CheckBox) findViewById(R.id.id_enable_smart_viberate_checkbox);
		CheckBox enableWifiBox = (CheckBox) findViewById(R.id.id_enable_wifi_checkbox);

		enableSmartViberateTextView.setOnClickListener(this);
		enableWifiTextView.setOnClickListener(this);
		updateCourseScheduleTextView.setOnClickListener(this);
		updateExamScheduleTextView.setOnClickListener(this);
		updateMapDataTextView.setOnClickListener(this);
		updateRestaurantDataTextView.setOnClickListener(this);
		enableSmartViberateBox.setOnClickListener(this);
		enableWifiBox.setOnClickListener(this);
	}

	// 实现标题栏的Home键
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// 实现标题栏的返回键
	public void btnBackOnClick(View v) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		UserJwcInfoDialogFragment userInfoDialog;
		switch (v.getId()) {
		case R.id.id_enable_smart_viberate_checkbox:
		case R.id.id_enable_smart_viberate_textview:
			break;
		case R.id.id_enable_wifi_checkbox:
		case R.id.id_enable_wifi_textview:
			break;
		case R.id.id_update_course_schedule: {
			userInfoDialog = new UserJwcInfoDialogFragment(PrefsActivity.this,
					JwcAction.UPDATE_COURSE_SCHEDULE, scheduleService);
			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
			break;
		}
		case R.id.id_update_exam_schedule:
			userInfoDialog = new UserJwcInfoDialogFragment(PrefsActivity.this,
					JwcAction.UPDATE_EXAM_SCHEDULE, scheduleService);
			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
			break;
		case R.id.id_update_map_data:
			restaurantService.initiateMapData(1);
			break;
		case R.id.id_update_restaurant_data:
			restaurantService.initiateRestaurantData(1);
			break;
		}

	}
}
