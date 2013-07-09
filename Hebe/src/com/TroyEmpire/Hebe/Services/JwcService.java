package com.TroyEmpire.Hebe.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.Environment;
import android.util.Log;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.ScheduleType;
import com.TroyEmpire.Hebe.Entities.ExamScore;
import com.TroyEmpire.Hebe.IServices.IJwcService;

public class JwcService implements IJwcService {

	private List<ExamScore> examScore = new ArrayList<ExamScore>();

	@Override
	public void downloadAndSaveCourseSchedule(
			final Map<String, String> userInfo, final Response response,
			final String validationCode, final String year,
			final String semester) {
		// Login to the JWC system
		Thread downloadThread = new Thread() {
			public void run() {
				try {
					downloadAndSaveSchedule(userInfo, response, validationCode,
							year, semester, ScheduleType.COURSESCHEDULE);
				} catch (Exception e) {
					Log.e("Notice", "Down load schedule error.", e);
				}
			};
		};
		Log.d("Notice", "Down load schedule success.");
		downloadThread.start();
		try {
			downloadThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void downloadAndSaveExamSchedule(final Map<String, String> userInfo,
			final Response response, final String validationCode,
			final String year, final String semester) {
		// Login to the JWC system
		Thread downloadThread = new Thread() {
			public void run() {
				try {
					downloadAndSaveSchedule(userInfo, response, validationCode,
							year, semester, ScheduleType.EXAMSCHEDULE);
				} catch (Exception e) {
					Log.e("Notice", "Down load schedule error.", e);
				}
			};
		};
		downloadThread.start();
		try {
			downloadThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int isUserLogInfoCorrect(Map<String, String> userInfo) {
		try {
			Connection jwcLoginUrlConnection = Jsoup
					.connect(Constant.JWC_LOGIN_WINDOW_URL);
			Connection.Response jwcLoginUrlGetResponse = jwcLoginUrlConnection
					.method(Method.GET).execute();
			Map<String, String> cookies = jwcLoginUrlGetResponse.cookies();
			// TODO ?
			Map<String, String> loginParameters = getExtraLoginParameters(
					jwcLoginUrlGetResponse.parse(), "Blurblur");
			loginParameters.putAll(userInfo);
			Connection.Response loginResponse = jwcLoginUrlConnection
					.cookies(cookies).method(Method.POST).data(loginParameters)
					.execute();
			if (loginResponse.parse().getElementsByTag("title").html()
					.equals(Constant.LOGIN_SUCCESS_RESPONSE_TITILE)) {
				return 0;
			}
		} catch (Exception e) {
			return 2;
		}
		return 1;
	}

	@Override
	public List<ExamScore> getExamScore(final Map<String, String> userInfo,
			final Response response, final String validationCode,
			final String year, final String semester) {
		Thread child = new Thread() {
			public void run() {
				Map<String, String> cookies2 = connectToJwc(userInfo, response,
						validationCode);
				Connection jwcExamScoreReceiveParameterConnection = Jsoup
						.connect(Constant.JWC_RECEIVE_EXAM_SCORE_PARAMETERS_URL);
				try {
					Connection.Response res1 = jwcExamScoreReceiveParameterConnection
							.cookies(cookies2).execute();
					Map<String, String> jwcExtraParameters = getExamScoreParameters(res1
							.parse());
					jwcExtraParameters.put(Constant.SCHEDULE_EXAM_SCORE_XN,
							year);
					jwcExtraParameters.put(Constant.SCHEDULE_EXAM_SCORE_XQ,
							semester);
					Document doc = jwcExamScoreReceiveParameterConnection
							.cookies(cookies2).method(Method.POST)
							.data(jwcExtraParameters).execute().parse();
					Element result = doc
							.getElementById(Constant.JWC_GET_EXAM_SCORE_TABLE_ID);
					formatExamScore(result.child(0));

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
		child.start();
		try {
			child.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.examScore;
	}

	private Map<String, String> connectToJwc(Map<String, String> userInfo,
			Response response, String validationCode) {
		Connection jwcLoginUrlConnection = Jsoup
				.connect(Constant.JWC_LOGIN_WINDOW_URL);
		try {
			Map<String, String> loginParameters = getExtraLoginParameters(
					response.parse(), validationCode);
			loginParameters.putAll(userInfo);
			Response responseRefresh = jwcLoginUrlConnection
					.cookies(response.cookies()).method(Method.POST)
					.data(loginParameters).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.cookies();
	}

	private void downloadAndSaveSchedule(final Map<String, String> userInfo,
			Response response, String validationCode, final String year,
			final String semester, ScheduleType SCHEEDULETYPE) throws Exception {
		// connect to JWC using password and user account
		Map<String, String> cookies2 = connectToJwc(userInfo, response,
				validationCode);
		// Connect to the Schedule main url
		Connection jwcScheduleMainUrlConnection = Jsoup
				.connect(Constant.JWC_RECEIVE_SCHEDULE_PARAMETERS_URL);
		Connection.Response res1 = jwcScheduleMainUrlConnection.cookies(
				cookies2).execute();
		// Gather all the post parameters for exporting schedule
		Map<String, String> jwcExtortScheduleParameters = getExtraScheduleParameters(res1
				.parse());
		jwcExtortScheduleParameters.put(Constant.SCHEDULE_SEMESTER, semester);
		jwcExtortScheduleParameters.put(Constant.SCHEDULE_YEAR, year);

		// Decide which schedule to export
		if (SCHEEDULETYPE == ScheduleType.COURSESCHEDULE)
			jwcExtortScheduleParameters.put(Constant.SCHEDULE_EXPORT_COURSE,
					"导出课程表");
		else
			jwcExtortScheduleParameters.put(Constant.SCHEDULE_EXPORT_EXAM,
					"导出考试表");

		// Send parameters to schedule url and receive the schedule
		jwcScheduleMainUrlConnection.cookies(cookies2).method(Method.POST)
				.data(jwcExtortScheduleParameters).execute();
		Connection jwcScheduleUrlIncludingExportOptions = Jsoup
				.connect(Constant.JWC_SCHEDULE_URL_INCLUDING_EXPORT_OPTIONS);
		Document doc = jwcScheduleUrlIncludingExportOptions.method(Method.GET)
				.cookies(cookies2).execute().parse();
		String pureSchedulePageurl = Jsoup
				.connect(
						Constant.JWC_MAIN_URL
								+ doc.getElementById("ReportFrameReportViewer1")
										.attr(Constant.SCHEDULE_EXPORT_INTERNAL_FRAME_ATTR))
				.method(Method.GET).cookies(cookies2).execute().parse()
				.getElementById("frameset").child(1)
				.attr(Constant.SCHEDULE_EXPORT_INTERNAL_FRAME_ATTR);
		String scheduleBody = Jsoup
				.connect(Constant.JWC_MAIN_URL + pureSchedulePageurl)
				.cookies(cookies2).execute().body();

		// Write the schedules to the file system
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File couseScheduleDirectory = new File(path
				+ Constant.SCHEDULE_RELATIVE_DIRECTORY);
		if (!couseScheduleDirectory.exists()) {
			couseScheduleDirectory.mkdirs();
		}
		if (SCHEEDULETYPE == ScheduleType.COURSESCHEDULE)
			path += Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
					+ Constant.COURSE_SCHEDULE_NAME;
		else
			path += Constant.SCHEDULE_RELATIVE_DIRECTORY + "/"
					+ Constant.EXAM_SCHEDULE_NAME;
		File schedule = new File(path);
		if (schedule.exists()) {
			schedule.delete();
		}
		schedule.createNewFile();
		FileOutputStream fos = new FileOutputStream(schedule);
		Writer out = new OutputStreamWriter(fos, "UTF-8");
		out.write(scheduleBody);
		out.close();
	}

	/**
	 * Get login parameters except username and password which can be read from
	 * plain html
	 * 
	 * @param loginDoc
	 *            the document of the login page
	 */
	private Map<String, String> getExtraLoginParameters(Document loginDoc,
			String validationCode) {
		Map<String, String> extraParameters = new HashMap<String, String>();
		String value, name;
		name = Constant.VIEWSTATE;
		value = loginDoc.getElementById("__VIEWSTATE").val();
		extraParameters.put(name, value);
		name = Constant.EVENTVALIDATION;
		value = loginDoc.getElementById("__EVENTVALIDATION").val();
		extraParameters.put(name, value);
		name = Constant.LOGIN;
		value = "登陆";
		extraParameters.put(name, value);
		name = Constant.CAPTCHA;
		value = validationCode;
		extraParameters.put(name, value);
		return extraParameters;
	}

	/**
	 * Get the parameters to export the course or exam schedule
	 * 
	 * @param
	 */
	private Map<String, String> getExtraScheduleParameters(Document scheduleDoc) {
		Map<String, String> extraParameters = new HashMap<String, String>();
		String value, name;
		name = Constant.VIEWSTATE;
		value = scheduleDoc.getElementById(Constant.VIEWSTATE).val();
		extraParameters.put(name, value);
		name = Constant.EVENTVALIDATION;
		value = scheduleDoc.getElementById(Constant.EVENTVALIDATION).val();
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_LASTFOCUS;
		value = "";
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_EVENTARGUMENT;
		value = "";
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_EVENTTARGET;
		value = "";
		extraParameters.put(name, value);
		return extraParameters;
	}

	private Map<String, String> getExamScoreParameters(Document scheduleDoc) {
		Map<String, String> extraParameters = new HashMap<String, String>();
		String value, name;
		name = Constant.VIEWSTATE;
		value = scheduleDoc.getElementById(Constant.VIEWSTATE).val();
		extraParameters.put(name, value);
		name = Constant.EVENTVALIDATION;
		value = scheduleDoc.getElementById(Constant.EVENTVALIDATION).val();
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_LASTFOCUS;
		value = "";
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_EVENTARGUMENT;
		value = "";
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_EVENTTARGET;
		value = Constant.SCHEDULE_EVENTTARGET_VALUE;
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_EXAM_SCORE_XH;
		value = scheduleDoc.getElementById(Constant.SCHEDULE_EXAM_SCORE_XH)
				.val();
		extraParameters.put(name, value);
		name = Constant.SCHEULE_EXAM_SCORE_XM;
		value = scheduleDoc.getElementById(Constant.SCHEULE_EXAM_SCORE_XM)
				.val();
		extraParameters.put(name, value);
		name = Constant.SCHEDULE_EXAM_SCORE_YXZY;
		value = scheduleDoc.getElementById(Constant.SCHEDULE_EXAM_SCORE_YXZY)
				.val();
		extraParameters.put(name, value);
		return extraParameters;
	}

	/*
	 * 下面的month > 8有三个，改月份的话要一起改哦
	 */

	@Override
	public String getCurrentSchoolYear() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		if (month > 8)
			return year + "-" + (year + 1);
		else
			return (year - 1) + "-" + year;
	}

	@Override
	public String getCurrentSemesterOneWord() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		if (month > 8)
			return "上";
		else
			return "下";
	}

	@Override
	public String getCurrentSemester() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		if (month > 8)
			return Constant.FIRST_SEMESTER;
		else
			return Constant.SECOND_SEMESTER;
	}

	private void formatExamScore(Element resultList) {
		this.examScore = new ArrayList<ExamScore>();
		List<Element> elements = resultList.children();
		// remove the first element which doesnot contain any exam info
		elements.remove(0);
		for (Element ele : elements) {
			ExamScore score = new ExamScore();
			score.setCourceName(ele.child(1).html());
			if (ele.child(2).html().trim().contains("&"))
				score.setScore(" ");
			else
				score.setScore(ele.child(2).html());
			score.setStatus(ele.child(4).html());
			score.setCreditPoints(ele.child(6).html());
			this.examScore.add(score);
		}
	}
}
