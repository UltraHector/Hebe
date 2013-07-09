package com.TroyEmpire.Hebe.Activities;

/**
 * 
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.JwcAction;
import com.TroyEmpire.Hebe.Constant.WeekDay;
import com.TroyEmpire.Hebe.Customized.UserJwcInfoDialogFragment;
import com.TroyEmpire.Hebe.Entities.DayCourseUnit;
import com.TroyEmpire.Hebe.IServices.IScheduleService;
import com.TroyEmpire.Hebe.Services.ScheduleService;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class IScheduleActivity extends FragmentActivity implements
		OnClickListener {

	IScheduleService scheduleService = new ScheduleService(this);
	WeekDay weekday;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ischedule);

		// Set the title bar text as IScehedule
		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.activity_ISchedule_label);

		// Set all the view monitor state
//		View updateSchedules = findViewById(R.id.button_ISchedule_Update);
//		updateSchedules.setOnClickListener(this);
		View dispalyCourseSchedule = findViewById(R.id.button_ISchedule_Update_Course_Schedule_Course);
		dispalyCourseSchedule.setOnClickListener(this);
		View displayExamSchedule = findViewById(R.id.button_ISchedule_Update_Exam_Schedule);
		displayExamSchedule.setOnClickListener(this);
		View displayExamScore = findViewById(R.id.button_ISchedule_Get_Exam_Score);
		displayExamScore.setOnClickListener(this);

		// No JWC information has been saved
		if (!scheduleService.userSchedulesHasBeenSaved()
				&& !scheduleService.isUserJwcPasswordHasBeenSaved()) {
			UserJwcInfoDialogFragment userInfoDialog = new UserJwcInfoDialogFragment(
					this, JwcAction.UPDATE_SCHEDULE,
					scheduleService);
			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
		}
		
		getWeekday();  //����ϵͳʱ��õ���ǰ�����ڼ�,��ֵ��weekday
		
		String today = weekday.toString();
		TextView tv = (TextView)findViewById(R.id.ischedule_today_tv);
		tv.setText(today);
		ListView list = (ListView)findViewById(R.id.ischedule_today_lv);
		
		//���ؽ������ݣ��󶨵�listItem��
		List<DayCourseUnit> courses = scheduleService.getDayCourseUnits(weekday);
		List<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
		for(DayCourseUnit course: courses){
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("time", course.getTimePeriod());
			map.put("courseName", course.getContent());
			listItem.add(map);
		}
		
		if(listItem.isEmpty()){
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("time", "ȫ��");
			map.put("courseName", "����������û�Σ��ú����ÿ���ʱ��ɣ����꣡");
			listItem.add(map);
		}
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
				R.layout.ischedule_today_item,
				new String[]{"time","courseName"},
				new int[]{R.id.ischedule_time_tv,R.id.ischedule_course_tv}
		);
		list.setAdapter(listItemAdapter);
	}

	private void getWeekday(){

		int weekdayFrom = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		switch(weekdayFrom){
		case 0:
			weekday = WeekDay.����;	
			break;
		case 1:
			weekday = WeekDay.��һ;	
			break;
		case 2:
			weekday = WeekDay.�ܶ�;
			break;
		case 3:
			weekday = WeekDay.����;	
			break;
		case 4:
			weekday = WeekDay.����;	
			break;
		case 5:
			weekday = WeekDay.����;	
			break;
		case 6:
			weekday = WeekDay.����;	
			break;
		}
	}

	@Override
	public void onClick(View v) {
		String schedulePath;
		UserJwcInfoDialogFragment userInfoDialog;
		switch (v.getId()) {
		
//		case R.id.button_ISchedule_Update:
//			userInfoDialog = new UserJwcInfoDialogFragment(
//					this, JwcAction.UPDATE_SCHEDULE,
//					scheduleService);
//			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
//			break;
		case R.id.button_ISchedule_Update_Course_Schedule_Course:
			Intent i1 = new Intent(this, IScheduleDisplayActivity.class);
			schedulePath = "file://" + scheduleService.getScheduleDirectory()
					+ "/" + Constant.COURSE_SCHEDULE_NAME;
			i1.putExtra("schedulePath", schedulePath);
			startActivity(i1);
			break;
		case R.id.button_ISchedule_Update_Exam_Schedule:
			Intent i2 = new Intent(this, IScheduleDisplayActivity.class);
			schedulePath = "file://" + scheduleService.getScheduleDirectory()
					+ "/" + Constant.EXAM_SCHEDULE_NAME;
			i2.putExtra("schedulePath", schedulePath);
			startActivity(i2);
			break;
		case R.id.button_ISchedule_Get_Exam_Score:
			userInfoDialog = new UserJwcInfoDialogFragment(
					this, JwcAction.GET_EXAM_SCORE,
					scheduleService);
			userInfoDialog.show(getSupportFragmentManager(), "getUserJwcInfo");
			break;
		}

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
