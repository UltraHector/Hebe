package com.TroyEmpire.Hebe.Customized;

import java.util.List;

import com.TroyEmpire.Hebe.Activities.R;
import com.TroyEmpire.Hebe.Entities.ExamScore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class IScheduleScoreAdapter extends BaseAdapter {

	private Context context;

	private List<ExamScore> list;

	/*
	 * �����гɼ�Ϊlist�����ұ���"�γ�����״̬��ѧ�֣��ɼ�"ҲҪ��ӡ������
	 * �����ó���0λ�ø����⣬
	 * list�ĳ���Ӧ��һ��ÿ�гɼ���λ��Ҳ�ı��ˣ�get(position - 1)������Ӧ�ĳɼ�
	 */
	public IScheduleScoreAdapter(Context context, List<ExamScore> list) {

		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.size();			//�����һ�еı��⣬list�ĳ���Ӧ��һ
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.ischedule_score_item, null);
			holder=new ViewHolder();
			
			convertView.setTag(holder);
			
			holder.tvCourseName=(TextView) convertView.findViewById(R.id.ischedule_score_item_coursename);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.ischedule_score_item_status);
			holder.tvCreditPoints = (TextView) convertView.findViewById(R.id.ischedule_score_item_creditpoints);
			holder.tvScore = (TextView) convertView.findViewById(R.id.ischedule_score_item_score);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		

			holder.tvCourseName.setText(list.get(position ).getCourceName());
			holder.tvStatus.setText(list.get(position).getStatus());
			holder.tvCreditPoints.setText(list.get(position).getCreditPoints());
			holder.tvScore.setText(list.get(position).getScore());
	
		return convertView;
	}

	static class ViewHolder {
		TextView tvCourseName;
		TextView tvStatus;
		TextView tvCreditPoints;
		TextView tvScore;
	}

}
