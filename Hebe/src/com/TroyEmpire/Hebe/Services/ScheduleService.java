package com.TroyEmpire.Hebe.Services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.WeekDay;
import com.TroyEmpire.Hebe.Entities.DayCourseUnit;
import com.TroyEmpire.Hebe.Entities.ExamScore;
import com.TroyEmpire.Hebe.IServices.IJwcService;
import com.TroyEmpire.Hebe.IServices.IScheduleService;

public class ScheduleService implements IScheduleService {

	private Activity activity;
	private int isUserInfoCorrectFlag = 0;
	private IJwcService jwcService = new JwcService();

	public ScheduleService(Activity activity) {
		this.activity = activity;
	}

	@Override
	public boolean isCourseScheduleSaved() {
		// check the whether schedules are in the file system
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File couseSchedule = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
				+ Constant.COURSE_SCHEDULE_NAME);
		if (couseSchedule.exists())
			return true;
		else
			return false;
	}

	@Override
	public boolean isExamScheduleSaved() {
		// check the whether schedules are in the file system
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File examSchedule = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
				+ Constant.EXAM_SCHEDULE_NAME);
		if (examSchedule.exists())
			return true;
		else
			return false;
	}

	@Override
	public boolean isAllScheduleSaved() {
		// check the whether schedules are in the file system
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File couseSchedule = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
				+ Constant.COURSE_SCHEDULE_NAME);
		File examSchedule = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
				+ Constant.EXAM_SCHEDULE_NAME);
		if (couseSchedule.exists() && examSchedule.exists())
			return true;
		else
			return false;
	}

	/**
	 * returen the path of the folder of schedules
	 */
	@Override
	public String getScheduleDirectory() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return path + Constant.SCHEDULE_RELATIVE_DIRECTORY;
	}

	@Override
	public void updateUserSchedule(Map<String, String> userJwcInfo,
			Response response, String validationCode) {
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.putAll(userJwcInfo);
		String year = jwcService.getCurrentSchoolYear();
		String semester = jwcService.getCurrentSemester();
		jwcService.downloadAndSaveCourseSchedule(userInfo, response,
				validationCode, year, semester);
		jwcService.downloadAndSaveExamSchedule(userInfo, response,
				validationCode, year, semester);
		updateFormattedSchdeule();

	}

	private void updateFormattedSchdeule() {
		SharedPreferences pref = activity.getSharedPreferences(
				Constant.SHARED_PREFERENCE_SCHEDULE, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY
				+ "/"
				+ Constant.COURSE_SCHEDULE_NAME;
		File courseSchedule = new File(path);
		try {
			Document doc = Jsoup.parse(courseSchedule, "UTF-8");
			// the table uses the class named "a8"
			List<Element> weekDays = doc.getElementsByClass("a8").get(0)
					.child(0).children();
			// remove first three
			weekDays.remove(0);
			weekDays.remove(0);
			weekDays.remove(0);
			// get and save the seven days schedule
			for (Element ele : weekDays) {
				String weekdayMark = ele.child(0).child(0).html().trim();
				String dayContent = "";
				List<Element> timeSlices = ele.children();
				timeSlices.remove(0); // remove 周几 mark
				int countSlice = 1;
				for (Element timeSlice : timeSlices) {
					if (!timeSlice.html().trim().startsWith("&")) {
						dayContent += countSlice + "#"
								+ timeSlice.child(0).html() + "$";
					}
					countSlice++;
				}
				editor.putString(weekdayMark, dayContent);
			}
			editor.commit();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isUserJwcAccountNumberHasBeenSaved() {
		SharedPreferences userInfo = activity.getSharedPreferences(
				Constant.USER_JWC_INFO, Context.MODE_PRIVATE);
		return userInfo.contains(Constant.USER_JWC_ACCOUNT_NUMBER);
	}

	@Override
	public boolean userSchedulesHasBeenSaved() {
		// check the whether schedules are in the file system
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File couseSchedule = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
				+ Constant.COURSE_SCHEDULE_NAME);
		File examSchedule = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
				+ Constant.EXAM_SCHEDULE_NAME);
		if (couseSchedule.exists() && examSchedule.exists())
			return true;
		else
			return false;
	}

	@Override
	public boolean isUserJwcPasswordHasBeenSaved() {
		SharedPreferences userInfo = activity.getSharedPreferences(
				Constant.USER_JWC_INFO, Context.MODE_PRIVATE);
		return userInfo.contains(Constant.USER_JWC_PASSWORD);
	}

	@Override
	public List<ExamScore> getExamScores(Map<String, String> userAccount,
			Response response, String validationCode) {
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.putAll(userAccount);
		String year = jwcService.getCurrentSchoolYear();
		// "上" means first semester, "下" means second semester
		String semester = jwcService.getCurrentSemesterOneWord();
		return jwcService.getExamScore(userInfo, response, validationCode,
				year, semester);
	}

	@Override
	public boolean removePassword() {
		SharedPreferences userInfo = activity.getSharedPreferences(
				Constant.USER_JWC_INFO, Context.MODE_PRIVATE);
		Editor editor = userInfo.edit();
		editor.remove(Constant.USER_JWC_PASSWORD);
		return editor.commit();
	}

	// raw String like : 1#电子商务南海楼$2#专家系统专家楼
	// return the formatted day course units
	@Override
	public List<DayCourseUnit> getDayCourseUnits(WeekDay weekDay) {
		SharedPreferences pref = activity.getSharedPreferences(
				Constant.SHARED_PREFERENCE_SCHEDULE, Context.MODE_PRIVATE);
		List<DayCourseUnit> dayCourseUnits = new ArrayList<DayCourseUnit>();
		String[] units = pref.getString(weekDay.toString(), "").split("['$']");
		for (int i = 0; i < units.length; i++) {
			DayCourseUnit dayCourseUnit = new DayCourseUnit();
			String timeSlice = ""; // record the time period of the course
			String content = ""; // record the course content
			String unit = units[i];
			if (unit.equals("") || unit == null)
				break;
			String atoms[] = unit.split("#");
			timeSlice += atoms[0];
			content = atoms[1];
			while (++i < units.length
					&& units[i].split("#")[1].equalsIgnoreCase(content)) {
				timeSlice += units[i].split("#")[0];
			}
			dayCourseUnit.setContent(content);
			dayCourseUnit.setTimePeriod(getTimePeriod(timeSlice));
			dayCourseUnits.add(dayCourseUnit);
		}
		return dayCourseUnits;
	}

	private String getTimePeriod(String slice) {
		String timePeriod = "";
		timePeriod += getTimePeriodByStartSliceId(slice.charAt(0) - 47);
		timePeriod += "-"
				+ getTimePeriodByEndSliceId(slice.charAt(slice.length() - 1) - 47);
		return timePeriod;
	}

	// 1 represents 08:00, 2 represents 09:00
	private String getTimePeriodByStartSliceId(int sliceId) {
		switch (sliceId) {
		case 1:
			return "08:00";
		case 2:
			return "09:00";
		case 3:
			return "10:00";
		case 4:
			return "11:00";
		case 5:
			return "12:00";
		case 6:
			return "13:00";
		case 7:
			return "14:00";
		case 8:
			return "15:00";
		case 9:
			return "16:00";
		case 10:
			return "17:00";
		case 11:
			return "18:00";
		case 12:
			return "19:00";
		case 13:
			return "20:00";
		case 14:
			return "21:00";
		}
		return "08:00";
	}

	// 1 represents 08:00, 2 represents 09:00
	private String getTimePeriodByEndSliceId(int sliceId) {
		switch (sliceId) {
		case 1:
			return "08:50";
		case 2:
			return "09:50";
		case 3:
			return "10:50";
		case 4:
			return "11:50";
		case 5:
			return "12:50";
		case 6:
			return "13:50";
		case 7:
			return "14:50";
		case 8:
			return "15:50";
		case 9:
			return "16:50";
		case 10:
			return "17:50";
		case 11:
			return "18:50";
		case 12:
			return "19:50";
		case 13:
			return "20:50";
		case 14:
			return "21:50";
		}
		return "08:50";
	}

	@Override
	public void updateUserCourseSchedule(Map<String, String> userJwcInfo,
			Response response, String validationCode) {
		IJwcService jwcService = new JwcService();
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.putAll(userJwcInfo);
		String year = jwcService.getCurrentSchoolYear();
		String semester = jwcService.getCurrentSemester();
		jwcService.downloadAndSaveCourseSchedule(userInfo, response,
				validationCode, year, semester);
		updateFormattedSchdeule();

	}

	@Override
	public void updateUserExamSchedule(Map<String, String> userJwcInfo,
			Response response, String validationCode) {
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.putAll(userJwcInfo);
		String year = jwcService.getCurrentSchoolYear();
		String semester = jwcService.getCurrentSemester();
		jwcService.downloadAndSaveExamSchedule(userInfo, response,
				validationCode, year, semester);

	}
}
