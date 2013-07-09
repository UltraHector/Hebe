package com.TroyEmpire.Hebe.Activities;

/**
 * 
 */

import java.util.ArrayList;
import java.util.List;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.NewsType;
import com.TroyEmpire.Hebe.Customized.PullDownView;
import com.TroyEmpire.Hebe.Customized.PullDownView.OnPullDownListener;
import com.TroyEmpire.Hebe.Customized.XinXiPTTitleAdapter;
import com.TroyEmpire.Hebe.Entities.News;
import com.TroyEmpire.Hebe.IServices.IInformationPlatformService;
import com.TroyEmpire.Hebe.Services.InformationPlatformSerivce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Hector
 * 
 */
public class XinXiPTActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;

	private TextView titleText;

	private PopupWindow popupWindow;
	private ListView groupListView;
	private View view;
	private List<String> groups;

	private ListView pullListView;
	private XinXiPTTitleAdapter pullAdapter;
	private PullDownView pullDownView;

	private IInformationPlatformService infoService;
	private List<News> listNews = new ArrayList<News>();

	private NewsType newsType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xinxipt);

		// Set the title bar text as this activity label
		titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.activity_XinXiPT_label);
		Drawable drawableTip = getResources().getDrawable(
				R.drawable.xinxipt_titlebar_tips1);
		drawableTip.setBounds(0, 0, 15, 15);
		titleText.setCompoundDrawables(null, null, null, drawableTip);
		// ������⣬��ʾ������
		View titleView = (View) findViewById(R.id.title_view);
		titleView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});

		infoService = new InformationPlatformSerivce(1, this);
		newsType = null;
		/*
		 * 1.ʹ��PullDownView 2.����OnPullDownListener 3.��mPullDownView�����ȡListView
		 */
		pullDownView = (PullDownView) findViewById(R.id.xinxipt_pull_down_view);
		pullDownView.setOnPullDownListener(this);

		pullListView = pullDownView.getListView();
		pullListView.setOnItemClickListener(this);

		pullAdapter = new XinXiPTTitleAdapter(this, listNews);
		pullListView.setAdapter(pullAdapter);

		pullDownView.enableAutoFetchMore(true, 1);

		loadData();
	}

	private Handler pullUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				pullAdapter.notifyDataSetChanged();
				pullDownView.notifyDidLoad();
				break;

			case WHAT_DID_REFRESH:
				pullAdapter.notifyDataSetChanged();
				pullDownView.notifyDidRefresh();
				break;

			case WHAT_DID_MORE:
				pullAdapter.notifyDataSetChanged();
				// ��������ȡ�������
				pullDownView.notifyDidMore();
				break;
			}

		}
	};

	private void loadData() {
		listNews.clear();
		listNews.addAll(infoService.getNewsFromStorage(newsType, null,
				Constant.NEWS_NUMBER_ONE_TIME_UPDATE__LIMIT));
		Log.d("load data", "" + newsType);
		if (listNews.isEmpty()) {
			Thread child = new Thread(new Runnable() {

				@Override
				public void run() {
					infoService.updateNews(newsType);
					listNews.clear();
					listNews.addAll(infoService.getNewsFromStorage(newsType,
							null, Constant.NEWS_NUMBER_ONE_TIME_UPDATE__LIMIT));
					Log.d("news size after update", "" + listNews.size());
					Message msg = pullUIHandler
							.obtainMessage(WHAT_DID_LOAD_DATA);
					msg.sendToTarget();
				}
			});
			child.start();
		} else {
			Log.d("news size", "" + listNews.size());
			Message msg = pullUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
			msg.sendToTarget();
		}

	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				infoService.updateNews(newsType);
				listNews.clear();
				listNews.addAll(infoService.getNewsFromStorage(newsType, null,
						Constant.NEWS_NUMBER_ONE_TIME_UPDATE__LIMIT));
				Log.d("news size after update", "" + listNews.size());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = pullUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Long stId = listNews.get(listNews.size() - 1).getId();
				if(newsType != null)
					Log.d("more:type",newsType.toString());
				listNews.addAll(infoService.getNewsFromStorage(newsType, stId,
						Constant.NEWS_NUMBER_ONE_TIME_UPDATE__LIMIT));
				Log.d("news size after load more", "" + listNews.size());
				Message msg = pullUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(this,
				listNews.get(position).getNewsType().toString() + position,
				Toast.LENGTH_SHORT).show();
		Intent displayNewsIntent = new Intent(this,
				XinXiPTDisplayNewsActivity.class);
		News news = listNews.get(position);
		displayNewsIntent.putExtra("newsSelected", news);
		startActivity(displayNewsIntent);
	}

	// ��ʧ�����
	private void showWindow(View v) {

		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = layoutInflater.inflate(R.layout.xinxipt_group, null);

			groupListView = (ListView) view
					.findViewById(R.id.xinxipt_group_listview);

			groups = new ArrayList<String>();
			groups.add("ȫ��");
			// groups.add("��ע����");
			groups.add("����");
			groups.add("ѧ����");
			// groups.add("У��֪ͨ");

			ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this,
					R.layout.xinxipt_group_item, groups);
			// XinXiPTGroupAdapter groupAdapter = new
			// XinXiPTGroupAdapter(this,groups);
			groupListView.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, 250, 310);
		}

		popupWindow.setFocusable(true); // ʹ��۽�
		popupWindow.setOutsideTouchable(true); // ����������������������ʧ
		popupWindow.setBackgroundDrawable(new BitmapDrawable(this
				.getResources())); // ���ؼ���ʧ
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;

		Log.i("coder", "windowManager.getDefaultDisplay().getWidth()/2:"
				+ windowManager.getDefaultDisplay().getWidth() / 2);
		//
		Log.i("coder", "popupWindow.getWidth()/2:" + popupWindow.getWidth() / 2);

		Log.i("coder", "xPos:" + xPos);

		popupWindow.showAsDropDown(v, xPos, 0); // ��ʾ֮

		// ѡ�������Ϣ
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				String selectedDep = groups.get(position);
				Toast.makeText(XinXiPTActivity.this,
						"groups.get(position)" + selectedDep, 1000).show();
				if ("ȫ��".equals(selectedDep)) {
					newsType = null;
				} else if ("����".equals(selectedDep)) {
					newsType = NewsType.����;
				} else if ("ѧ����".equals(selectedDep)) {
					newsType = NewsType.ѧ����;
				} else {
					newsType = null;
				}
				titleText.setText(selectedDep);

				loadData();

				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	// ʵ�ֱ�������Home��
	public void btnHomeOnClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}

	// ʵ�ֱ������ķ��ؼ�
	public void btnBackOnClick(View v) {
		finish();
	}

}
