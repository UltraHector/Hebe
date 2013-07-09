package com.TroyEmpire.Hebe.Helpers;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.DBConstant;
import com.TroyEmpire.Hebe.Entities.Building;
import com.TroyEmpire.Hebe.Entities.Cell;
import com.TroyEmpire.Hebe.Entities.PathDot;

public class MapServiceHelper {

	private String TAG = "Map database";
	private final String hebeSDCardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constant.HEBE_STORAGE_ROOT + "/Map";

	private String dbFile;// Map
	private SQLiteDatabase db;

	public Activity activity;

	public MapServiceHelper(Activity activity) {
		this.activity = activity;
		// campusId must be first stored in XiaoYuanDTWebView.java
		// open database
		SharedPreferences sp = activity.getSharedPreferences(
				Constant.SHARED_PREFERENCE_MAP_CAMPUS_ID_FILE,
				Context.MODE_PRIVATE);
		String campusId = sp.getString(
				Constant.SHARED_PREFERENCE_MAP_CMAPUS_ID_KEY, "");
		if (campusId.equals("")) {
			Log.e(TAG, "error: can not found campus ID");
		} else {

			dbFile = hebeSDCardPath + "/Campus_" + campusId
					+ "_Map/MapDB/Map.db";

			db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

		}

	}

	public PathDot getPathDotById(int id) {
		String[] columns = { DBConstant.TABLE_PATH_DOT_FIELD_LATITUDE,
				DBConstant.TABLE_PATH_DOT_FIELD_LONGITUDE };
		String selection = DBConstant.TABLE_PATH_DOT_FIELD_ID + "=" + id;
		PathDot dot = new PathDot();
		try {
			Cursor cursor = (Cursor) db.query(DBConstant.TABLE_PATH_DOT,
					columns, selection, null, null, null, null);
			activity.startManagingCursor(cursor);

			if (cursor.moveToNext()) {
				double latitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_PATH_DOT_FIELD_LATITUDE));
				double longitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_PATH_DOT_FIELD_LONGITUDE));
				dot.setLatitude(latitude);
				dot.setLongitude(longitude);
			} else
				return null;//
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dot;
	}

	// if exception is being caught then return null
	public Cell getCellById(int id) {
		final String[] columns = { DBConstant.TABLE_CELL_FIELD_ID,
				DBConstant.TABLE_CELL_FIELD_NAME,
				DBConstant.TABLE_CELL_FIELD_BUILDINGID };
		final String selection = DBConstant.TABLE_CELL_FIELD_ID + " = "
				+ String.valueOf(id);

		Cell cell = new Cell();
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_CELL, columns,
				selection, null, null, null, null);
		activity.startManagingCursor(cursor);
		try {
			if (cursor.moveToNext()) {
				int cellId = id;
				String cellName = cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_CELL_FIELD_NAME));
				int buildingId = cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_CELL_FIELD_NAME));

				cell.setId(cellId);
				cell.setName(cellName);
				cell.setBuildingId(buildingId);
				return cell;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return cell;
	}

	// if exception is being caught then return null
	public Building getBuildingById(int id) {
		final String[] columns = { DBConstant.TABLE_BUILDING_FIELD_ID,
				DBConstant.TABLE_BUILDING_FIELD_LATITUDE,
				DBConstant.TABLE_BUILDING_FIELD_LONGITUDE,
				DBConstant.TABLE_BUILDING_FIELD_DESCRIPTION,
				DBConstant.TABLE_BUILDING_FIELD_NAME,
				DBConstant.TABLE_BUILDING_FIELD_MAXLATITUDE,
				DBConstant.TABLE_BUILDING_FIELD_MAXLONGITUDE,
				DBConstant.TABLE_BUILDING_FIELD_MINLATITUDE,
				DBConstant.TABLE_BUILDING_FIELD_MINLONGITUDE,
				DBConstant.TABLE_BULIDING_FIELD_PATHDOTID };
		final String selection = DBConstant.TABLE_BUILDING_FIELD_ID + " = "
				+ String.valueOf(id);

		Building building = new Building();
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_BUILDING, columns,
				selection, null, null, null, null);
		activity.startManagingCursor(cursor);
		try {
			if (cursor.moveToNext()) {
				int buildingId = cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_ID));
				double latitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_LATITUDE));
				double longitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_LONGITUDE));
				String description = cursor
						.getString(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_DESCRIPTION));
				double maxLatitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_MAXLATITUDE));
				double maxLongitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_MAXLONGITUDE));
				double minLatitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_MINLATITUDE));
				double minLongitude = cursor
						.getDouble(cursor
								.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_MINLONGITUDE));
				String name = cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_NAME));
				int pathDotId = cursor
						.getInt(cursor
								.getColumnIndex(DBConstant.TABLE_BULIDING_FIELD_PATHDOTID));

				building.setId(buildingId);
				building.setLatitude(latitude);
				building.setLongitude(longitude);
				building.setDescription(description);
				building.setMaxLatitude(maxLatitude);
				building.setMaxLongitude(maxLongitude);
				building.setMiniLatitude(minLatitude);
				building.setMiniLongitude(minLongitude);
				building.setName(name);
				building.setPathDotId(pathDotId);
				return building;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return building;
	}

	// id string ,format :1#5#9#3#10
	public String getShortestPath(long sourceId, long destId) {
		// make sure the sourceId is smaller than destId
		if (sourceId > destId) {
			long temp = sourceId;
			sourceId = destId;
			destId = temp;
		}
		final String[] columns = { DBConstant.TABLE_SHORTEST_PATH_FIELD_SHORTEST_PATH };
		final String condition = DBConstant.TABLE_SHORTEST_PATH_FIELD_SOURCE_ID
				+ "=" + sourceId + " and "
				+ DBConstant.TABLE_SHORTEST_PATH_FIELD_DEST_ID + "=" + destId;
		Cursor cursor = db.query(DBConstant.TABLE_SHORTEST_PATH, columns,
				condition, null, null, null, null);
		activity.startManagingCursor(cursor);
		if (cursor.moveToFirst())
			return cursor
					.getString(cursor
							.getColumnIndex(DBConstant.TABLE_SHORTEST_PATH_FIELD_SHORTEST_PATH));
		// else return null
		return "";

	}

	// return top n order by hitcount desc
	public List<Cell> getTopNCell(int n) {

		Cursor cursor = db.rawQuery("select "
				+ DBConstant.TABLE_FREQUENT_PLACE_FIELD_CELL_ID + " from "
				+ DBConstant.TABLE_FREQUENT_PLACE + "," + DBConstant.TABLE_CELL
				+ " where " + DBConstant.TABLE_FREQUENT_PLACE + "."
				+ DBConstant.TABLE_FREQUENT_PLACE_FIELD_CELL_ID + "="
				+ DBConstant.TABLE_CELL + "." + DBConstant.TABLE_CELL_FIELD_ID
				+ " order by "
				+ DBConstant.TABLE_FREQUENT_PLACE_FIELD_HIT_COUNT
				+ " desc limit " + String.valueOf(n), null);
		activity.startManagingCursor(cursor);

		List<Cell> cellList = new ArrayList<Cell>();
		cellList.clear();

		// Log.i(TAG,cursor.getColumnName(0));

		while (cursor.moveToNext()) {
			int id = cursor
					.getInt(cursor
							.getColumnIndex(DBConstant.TABLE_FREQUENT_PLACE_FIELD_CELL_ID));
			Cell cell = this.getCellById(id);
			// System.out.println(cell.getName());

			cellList.add(cell);
		}

		cursor.close();
		return cellList;
	}

	/**
	 * @param pattern
	 *            : for search n:list at most n cells
	 * @return List of Cells
	 * */
	public List<String> getSuggestionCellsName(String pattern, int n) {
		String[] columns = { DBConstant.TABLE_CELL_FIELD_ID,
				DBConstant.TABLE_CELL_FIELD_NAME,
				DBConstant.TABLE_CELL_FIELD_BUILDINGID };
		String selection = "name like \'" + pattern + "%\'";
		Cursor cursor = db.query(DBConstant.TABLE_CELL, columns, selection,
				null, null, null, null);

		List<String> cellList = new ArrayList<String>();
		int i = 0;
		while (cursor.moveToNext()) {
			// int id =
			// cursor.getInt(cursor.getColumnIndex(DBConstant.TABLE_CELL_FIELD_ID));
			String name = cursor.getString(cursor
					.getColumnIndex(DBConstant.TABLE_CELL_FIELD_NAME));
			// int buildingId =
			// cursor.getInt(cursor.getColumnIndex(DBConstant.TABLE_CELL_FIELD_BUILDINGID));

			cellList.add(name);

			i++;
			if (i > n) {
				break;
			}
		}
		cursor.close();

		return cellList;

	}
}
