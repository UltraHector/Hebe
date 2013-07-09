package com.TroyEmpire.Hebe.Services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.DBConstant;
import com.TroyEmpire.Hebe.Constant.NewsType;
import com.TroyEmpire.Hebe.Entities.News;
import com.TroyEmpire.Hebe.Entities.NewsOnServer;
import com.TroyEmpire.Hebe.IServices.IInformationPlatformService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class InformationPlatformSerivce implements IInformationPlatformService {

	// database path
	private final String hebeSDCardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constant.HEBE_STORAGE_ROOT + "/InformationPlatform";
	// columns names
	private final String[] columnsNews = new String[] {
			DBConstant.TABLE_NEWS_FIELD_ID, DBConstant.TABLE_NEWS_FIELD_TITLE,
			DBConstant.TABLE_NEWS_FIELD_CONTENT,
			DBConstant.TABLE_NEWS_FIELD_NEWS_ID_ON_SERVER,
			DBConstant.TABLE_NEWS_FIELD_NEWS_TYPE,
			DBConstant.TABLE_NEWS_FIELD_PUBLISH_DATE };
	private int campusId;
	private String dbFile;
	private SQLiteDatabase db;
	private Activity activity;

	public InformationPlatformSerivce(int campusId, Activity activity) {
		this.campusId = campusId;
		this.activity = activity;
		createStorageDirectory();
		dbFile = this.hebeSDCardPath + "/Campus_" + campusId
				+ "_InformationPlatform/Information.db";
		this.db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		if (!isNewsTableExist()) {
			createNewsTable(); // if not exist, create a new table
		} else {
			cleanNewsCache();
		}
	}


	// 当缓存的信息过多是，清理newstable的缓存。
	private void cleanNewsCache() {
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_NEWS,
				new String[] { DBConstant.TABLE_NEWS_FIELD_ID }, null, null,
				null, null, " id desc ",
				String.valueOf(Constant.NEWS_CACHE_LIMIT));
		activity.startManagingCursor(cursor);
		if (cursor.moveToLast()) {
			int markId = cursor.getInt(cursor
					.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_ID));
			String query = "delete from " + DBConstant.TABLE_NEWS
					+ " where id<" + markId;
			db.execSQL(query);
		}
	}

	private void createStorageDirectory() {
		String path = this.hebeSDCardPath + "/Campus_" + campusId
				+ "_InformationPlatform";
		File directory = new File(path);
		if (!directory.exists())
			directory.mkdirs();
		File dataBaseFile = new File(path + "/Information.db");
		if (!dataBaseFile.exists())
			try {
				dataBaseFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void updateNews(NewsType type) {
		String url = Constant.HEBE_SERVER_URL + "/news";
		List<News> newsList = new ArrayList<News>();

		if (type == null)
			url += "/" + NewsType.预制.ordinal() + "/" + getMaxNewsServerId(type); // parse
																					// 2
																					// means
																					// update
		// all types of news
		else
			url += "/" + type.ordinal() + "/" + getMaxNewsServerId(type);
		try {
			InputStream input = new URL(url).openStream();
			Reader reader = new InputStreamReader(input, "UTF-8");
			// Creates the json object which will manage the information
			// received
			GsonBuilder builder = new GsonBuilder();
			// Register an adapter to manage the date types as long values
			builder.registerTypeAdapter(Date.class,
					new JsonDeserializer<Date>() {
						@Override
						public Date deserialize(JsonElement json, Type typeOfT,
								JsonDeserializationContext context)
								throws JsonParseException {
							return new Date(json.getAsJsonPrimitive()
									.getAsLong());
						}
					});
			Gson json = builder.create();
			NewsOnServer[] newsOnServer = json.fromJson(reader,
					NewsOnServer[].class);
			for (NewsOnServer rawNews : newsOnServer) {
				News news = new News();
				news.setContent(rawNews.getContent());
				news.setNewsIdOnServer(rawNews.getId());
				news.setNewsType(rawNews.getNewsType());
				news.setPublishDate(rawNews.getPublishDate());
				news.setTitle(rawNews.getTitle());
				newsList.add(news);
				insertAllNewValues(newsList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private long getMaxNewsServerId(NewsType type) {
		String condition = "";
		if (type == null)
			condition = null;
		else {
			switch (type) {
			case 教务处:
				condition += " newsType=" + NewsType.教务处.ordinal();
				break;
			case 学生处:
				condition += " newsType=" + NewsType.学生处.ordinal();
				break;
			}
		}
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_NEWS, columnsNews,
				condition, null, null, null, " id desc ", "1");
		activity.startManagingCursor(cursor);
		News news = new News();
		if (cursor.moveToFirst()) {
			setNewsProperties(news, cursor);
		}
		return news.getNewsIdOnServer();
	}

	// if the newstype is passed as null, all news will be updated
	public List<News> getNewsFromStorage(NewsType type, Long startId, int limit) {
		String condition = null;
		if (type != null) {
			condition = DBConstant.TABLE_NEWS_FIELD_NEWS_TYPE + "="
					+ type.ordinal() + " ";
		}
		if (startId == null) {
			List<News> newsList = (List<News>) findNewsList(condition,
					" id DESC ", String.valueOf(limit));
			return newsList;
		}
		// need to set it to avoid null + "";
		if(condition == null){
			condition = "";
			condition += " id < " + startId;
		}
		else{
			condition += " and id < " + startId;
		}
		List<News> newsList = (List<News>) findNewsList(condition, " id DESC ",
				String.valueOf(limit));
		return newsList;
	}

	private boolean isNewsTableExist() {
		try {
			db.query(false, DBConstant.TABLE_NEWS, columnsNews, null, null,
					null, null, " id ", "1");
			return true;
		} catch (Exception e) {
			Log.i("No News table", "Will Create A New One");
			return false;
		}
	}

	private void insertAllNewValues(List<News> newsList) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		ContentValues values = new ContentValues();
		for (News news : newsList) {
			values.put(DBConstant.TABLE_NEWS_FIELD_CONTENT, news.getContent());
			values.put(DBConstant.TABLE_NEWS_FIELD_NEWS_ID_ON_SERVER,
					news.getNewsIdOnServer());
			values.put(DBConstant.TABLE_NEWS_FIELD_NEWS_TYPE, news
					.getNewsType().ordinal());
			values.put(DBConstant.TABLE_NEWS_FIELD_PUBLISH_DATE, formater
					.format(news.getPublishDate()).toString());
			values.put(DBConstant.TABLE_NEWS_FIELD_TITLE, news.getTitle());
		}
		db.insert(DBConstant.TABLE_NEWS, null, values);
	}

	private void insertNewValues(News news) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		ContentValues values = new ContentValues();
		values.put(DBConstant.TABLE_NEWS_FIELD_CONTENT, news.getContent());
		values.put(DBConstant.TABLE_NEWS_FIELD_NEWS_ID_ON_SERVER,
				news.getNewsIdOnServer());
		values.put(DBConstant.TABLE_NEWS_FIELD_NEWS_TYPE, news.getNewsType()
				.ordinal());
		values.put(DBConstant.TABLE_NEWS_FIELD_PUBLISH_DATE,
				formater.format(news.getPublishDate()).toString());
		values.put(DBConstant.TABLE_NEWS_FIELD_TITLE, news.getTitle());
		db.insert(DBConstant.TABLE_NEWS, null, values);
	}

	private void createNewsTable() {
		String query = "create table " + DBConstant.TABLE_NEWS + "("
				+ DBConstant.TABLE_NEWS_FIELD_ID
				+ " INTEGER primary key autoincrement, "
				+ DBConstant.TABLE_NEWS_FIELD_TITLE + " text, "
				+ DBConstant.TABLE_NEWS_FIELD_NEWS_TYPE + " INTEGER, "
				+ DBConstant.TABLE_NEWS_FIELD_NEWS_ID_ON_SERVER + " INTEGER,"
				+ DBConstant.TABLE_NEWS_FIELD_PUBLISH_DATE + " text, "
				+ DBConstant.TABLE_NEWS_FIELD_CONTENT + " text)";
		db.execSQL(query);
	}

	@SuppressWarnings("deprecation")
	private List<News> findNewsList(String condition, String orderBy,
			String limit) {
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_NEWS, columnsNews,
				condition, null, null, null, orderBy, limit);
		activity.startManagingCursor(cursor);
		List<News> newsList = new ArrayList<News>();
		while (cursor.moveToNext()) {
			News news = new News();
			setNewsProperties(news, cursor);
			newsList.add(news);
		}
		return newsList;
	}

	@SuppressWarnings("deprecation")
	private News findSingleNews(String condition) {
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_NEWS, columnsNews,
				condition, null, null, null, null);
		activity.startManagingCursor(cursor);
		News news = new News();
		if (cursor.moveToFirst()) {
			setNewsProperties(news, cursor);
		}
		return news;
	}

	private void setNewsProperties(News news, Cursor cursor) {
		NewsType type = null;
		int newsType = cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_NEWS_TYPE));
		if (newsType == NewsType.教务处.ordinal())
			type = NewsType.教务处;
		else if (newsType == NewsType.学生处.ordinal())
			type = NewsType.学生处;
		else
			type = NewsType.预制;
		news.setId(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_ID)));

		news.setNewsIdOnServer(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_NEWS_ID_ON_SERVER)));
		news.setContent(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_CONTENT)));
		news.setTitle(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_TITLE)));
		try {
			news.setPublishDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.CHINA).parse(cursor.getString(cursor
					.getColumnIndex(DBConstant.TABLE_NEWS_FIELD_PUBLISH_DATE))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		news.setNewsType(type);
	}

	@Override
	public News getNewsByid(long id) {
		String condition = DBConstant.TABLE_NEWS_FIELD_ID + "=" + id;
		return findSingleNews(condition);
	}
}
