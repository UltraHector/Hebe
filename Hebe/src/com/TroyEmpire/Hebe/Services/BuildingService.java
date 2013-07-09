package com.TroyEmpire.Hebe.Services;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.DBConstant;
import com.TroyEmpire.Hebe.Entities.Building;
import com.TroyEmpire.Hebe.IServices.IBuildingService;

public class BuildingService implements IBuildingService {

	private final String[] buildingColumns = new String[] {
			DBConstant.TABLE_BUILDING_FIELD_ID,
			DBConstant.TABLE_BUILDING_FIELD_NAME,
			DBConstant.TABLE_BUILDING_FIELD_LATITUDE,
			DBConstant.TABLE_BULIDING_FIELD_PATHDOTID,
			DBConstant.TABLE_BUILDING_FIELD_LONGITUDE,
			DBConstant.TABLE_BUILDING_FIELD_DESCRIPTION,
			DBConstant.TABLE_BUILDING_FIELD_MINLATITUDE,
			DBConstant.TABLE_BUILDING_FIELD_MAXLATITUDE,
			DBConstant.TABLE_BUILDING_FIELD_MINLONGITUDE,
			DBConstant.TABLE_BUILDING_FIELD_MAXLONGITUDE };

	private final String hebeSDCardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constant.HEBE_STORAGE_ROOT + "/Map";
	private int campusId;
	private String dbFile;
	private SQLiteDatabase db;
	private Activity activity;

	public BuildingService(int campusId, Activity activity) {
		this.campusId = campusId;
		this.activity = activity;
		this.dbFile = hebeSDCardPath + "/Campus_" + campusId
				+ "_Map/MapDB/Map.db";
		this.db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
	}

	@Override
	public Building getBuildingByName(String cellName) {
		int buildingId = 0;
		// get the cell
		String[] cellColumns = new String[] { DBConstant.TABLE_CELL_FIELD_BUILDINGID };
		String cellArgs = DBConstant.TABLE_CELL_FIELD_NAME + "= \'" + cellName
				+ "\'";
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_CELL, cellColumns,
				cellArgs, null, null, null, null);
		if (cursor.moveToFirst()) {
			buildingId = Integer.valueOf(cursor.getString(0));
		}
		String condition = DBConstant.TABLE_BUILDING_FIELD_ID + "="
				+ buildingId;
		return getBuilding(condition);
	}

	@Override
	public Building getBuildingByLocation(double latitude, double longitude) {
		String condition = DBConstant.TABLE_BUILDING_FIELD_MINLATITUDE + "<"
				+ latitude + " and "
				+ DBConstant.TABLE_BUILDING_FIELD_MAXLATITUDE + ">" + latitude
				+ " and " + DBConstant.TABLE_BUILDING_FIELD_MINLONGITUDE + "<"
				+ longitude + " and "
				+ DBConstant.TABLE_BUILDING_FIELD_MAXLONGITUDE + ">"
				+ longitude;

		return getBuilding(condition);
	}

	@SuppressWarnings("deprecation")
	private Building getBuilding(String condition) {
		Building building = new Building();
		Cursor cursor = (Cursor) db.query(DBConstant.TABLE_BUILDING,
				buildingColumns, condition, null, null, null, null);

		activity.startManagingCursor(cursor);

		if (cursor.moveToFirst()) {
			building.setId(cursor.getLong(cursor
					.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_ID)));
			building.setName(cursor.getString(cursor
					.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_NAME)));
			building.setLatitude(cursor.getDouble(cursor
					.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_LATITUDE)));
			building.setLongitude(cursor.getDouble(cursor
					.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_LONGITUDE)));
			building.setDescription(cursor.getString(cursor
					.getColumnIndex(DBConstant.TABLE_BUILDING_FIELD_DESCRIPTION)));
			building.setPathDotId(cursor.getInt(cursor
					.getColumnIndex(DBConstant.TABLE_BULIDING_FIELD_PATHDOTID)));
		}

		return building;
	}

}
