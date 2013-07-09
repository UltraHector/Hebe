package com.TroyEmpire.Hebe.Services;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.DBConstant;
import com.TroyEmpire.Hebe.IServices.IShortestPathService;

public class ShortestPathService implements IShortestPathService {

	private final String[] shortestPathColumns = new String[] {
			DBConstant.TABLE_SHORTEST_PATH_FIELD_ID,
			DBConstant.TABLE_SHORTEST_PATH_FIELD_DEST_ID,
			DBConstant.TABLE_SHORTEST_PATH_FIELD_SOURCE_ID,
			DBConstant.TABLE_SHORTEST_PATH_FIELD_SHORTEST_PATH };
	private final String hebeSDCardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constant.HEBE_STORAGE_ROOT + "/Map";

	// set the datebase
	private int campusId;
	private String dbFile;
	private SQLiteDatabase db;
	private Activity activity;

	public ShortestPathService(int campusId, Activity activity) {
		this.campusId = campusId;
		this.activity = activity;
		this.dbFile = hebeSDCardPath + "/Campus_" + campusId
				+ "_Map/MapDB/Map.db";
		this.db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
	}

	@Override
	public String getShortestPath(int sourceId, int destId) {
		// make sure the sourceId is smaller than destId
		if (sourceId > destId) {
			int temp = sourceId;
			sourceId = destId;
			destId = temp;
		}
		String condition = DBConstant.TABLE_SHORTEST_PATH_FIELD_SOURCE_ID + "="
				+ sourceId + " and "
				+ DBConstant.TABLE_SHORTEST_PATH_FIELD_DEST_ID + "=" + destId;
		Cursor cursor = db.query(DBConstant.TABLE_SHORTEST_PATH,
				shortestPathColumns, condition, null, null, null, null);
		activity.startManagingCursor(cursor);
		if (cursor.moveToFirst())
			return cursor
					.getString(cursor
							.getColumnIndex(DBConstant.TABLE_SHORTEST_PATH_FIELD_SHORTEST_PATH));
		// else return null
		return "";

	}

}
