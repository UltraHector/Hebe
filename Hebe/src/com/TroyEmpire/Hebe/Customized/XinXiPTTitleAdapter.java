package com.TroyEmpire.Hebe.Customized;

import java.text.SimpleDateFormat;
import java.util.List;

import com.TroyEmpire.Hebe.Activities.R;
import com.TroyEmpire.Hebe.Entities.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public 	class XinXiPTTitleAdapter extends BaseAdapter {

	private Context context;

	 private List<News> listNews;

	public XinXiPTTitleAdapter(Context context,List<News> list) {

		this.context = context;
		this.listNews = list;

	}

	@Override
	public int getCount() {
		return listNews.size();
	}

	@Override
	public Object getItem(int position) {

		return listNews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.xinxipt_pulldown_item, null);
			holder = new ViewHolder();

			convertView.setTag(holder);

			holder.newsTitleTV = (TextView) convertView
					.findViewById(R.id.xinxipt_title_textview);
			holder.newsDateTV = (TextView) convertView
					.findViewById(R.id.xinxipt_news_date_textview);
			holder.newsTypeTV = (TextView) convertView
					.findViewById(R.id.xinxipt_news_type_textview);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		holder.newsTitleTV.setText(listNews.get(position).getTitle());
		holder.newsDateTV.setText(formater.format(listNews.get(position).getPublishDate()));
		holder.newsTypeTV.setText(listNews.get(position).getNewsType().toString());

		return convertView;
	}

	static class ViewHolder {
		TextView newsTitleTV;
		TextView newsDateTV;
		TextView newsTypeTV;
	}
}

