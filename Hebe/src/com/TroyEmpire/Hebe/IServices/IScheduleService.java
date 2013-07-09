package com.TroyEmpire.Hebe.IServices;

import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Response;

import com.TroyEmpire.Hebe.Constant.WeekDay;
import com.TroyEmpire.Hebe.Entities.DayCourseUnit;
import com.TroyEmpire.Hebe.Entities.ExamScore;

public interface IScheduleService {
	public boolean isCourseScheduleSaved();

	public boolean isExamScheduleSaved();

	public boolean isAllScheduleSaved();

	public String getScheduleDirectory();

	public void updateUserSchedule(Map<String, String> userInfo,
			Response response, String validationCode);

	public void updateUserCourseSchedule(Map<String, String> userInfo,
			Response response, String validationCode);

	public void updateUserExamSchedule(Map<String, String> userInfo,
			Response response, String validationCode);

	public boolean isUserJwcPasswordHasBeenSaved();

	public boolean userSchedulesHasBeenSaved();

	public boolean isUserJwcAccountNumberHasBeenSaved();

	public List<ExamScore> getExamScores(Map<String, String> userInfo,
			Response response, String validationCode);

	public boolean removePassword();

	public List<DayCourseUnit> getDayCourseUnits(WeekDay weekDay);
}
