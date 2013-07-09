package com.TroyEmpire.Hebe.IServices;

import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Response;

import com.TroyEmpire.Hebe.Entities.ExamScore;

public interface IJwcService {
	public void downloadAndSaveCourseSchedule(Map<String, String> userInfo,
			final Response response, final String validationCode,
			String year, String semester);

	public void downloadAndSaveExamSchedule(Map<String, String> userInfo,
			final Response response, final String validationCode,
			String year, String semester);

	/**
	 * @param userInfo
	 * @return 0 every thing is ok 1 when the password is wrong, 2 when the
	 *         network error, 3 other problem
	 */
	public int isUserLogInfoCorrect(Map<String, String> userInfo);

	public String getCurrentSchoolYear();

	public String getCurrentSemester();

	public String getCurrentSemesterOneWord();

	public List<ExamScore> getExamScore(Map<String, String> userInfo,
			final Response response, final String validationCode,
			String year, String semester);
}
