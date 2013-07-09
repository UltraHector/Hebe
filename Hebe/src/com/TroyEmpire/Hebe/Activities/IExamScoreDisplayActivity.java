package com.TroyEmpire.Hebe.Activities;

import java.util.ArrayList;
import java.util.List;
import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Customized.IScheduleScoreAdapter;
import com.TroyEmpire.Hebe.Entities.ExamScore;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class IExamScoreDisplayActivity extends Activity {

	private ListView listView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_exam_score);

		TextView tvTitleBar = (TextView) findViewById(R.id.title_text);
		tvTitleBar.setText("本学期成绩单");

		// 配置listView的adapter
		listView = (ListView) findViewById(R.id.ischedule_score_lv);
		List<ExamScore> listScores = this.getExamScoresFromIntent();
		IScheduleScoreAdapter adapter = new IScheduleScoreAdapter(this,
				listScores);
		listView.setAdapter(adapter);
	}

	private List<ExamScore> getExamScoresFromIntent() {
		List<ExamScore> examScores = new ArrayList<ExamScore>();
		int numberOfExamScores = this.getIntent().getExtras()
				.getInt(Constant.NUMBER_OF_EXAM_SCORES);
		for (int i = numberOfExamScores; i > 0; i--) {
			examScores.add((ExamScore) this.getIntent().getExtras()
					.get("examScores_" + i));
		}
		return examScores;
	}

	// 实现标题栏的Home键
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// 实现标题栏的返回键
	public void btnBackOnClick(View v) {
		finish();
	}
}
