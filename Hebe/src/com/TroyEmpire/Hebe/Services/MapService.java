package com.TroyEmpire.Hebe.Services;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Entities.Building;
import com.TroyEmpire.Hebe.Entities.Cell;
import com.TroyEmpire.Hebe.Entities.PathDot;
import com.TroyEmpire.Hebe.Helpers.MapServiceHelper;
import com.TroyEmpire.Hebe.IServices.IMapService;

public class MapService implements IMapService {

	private String TAG = "MapService";

	private MapServiceHelper mapServiceHelper;

	public Activity activity;

	public MapService(Activity activity) {
		this.activity = activity;
		mapServiceHelper = new MapServiceHelper(activity);
	}

	@Override
	public List<Cell> getSearchHistory() {
		List<Cell> cellList = new ArrayList<Cell>();

		SharedPreferences sp = activity.getSharedPreferences(
				Constant.SHARED_PREFERENCE_MAP_HISTORY_FILE,
				Context.MODE_PRIVATE);
		String cellIdString = sp.getString(
				Constant.SHARED_PREFERENCE_MAP_HSITORY_KEY, "");
		
		if(cellIdString.equals(""))
			return cellList;
		String[] cellId = cellIdString.split("#");

		cellList.clear();
		
		for (int i = 0; i < cellId.length; i++) {
			Cell cell = mapServiceHelper.getCellById(Integer
					.parseInt(cellId[i]));
			cellList.add(cell);
		}
		return cellList;

	}

	@Override
	public List<Cell> getFrequentPlace() {

		return mapServiceHelper.getTopNCell(Integer
				.parseInt(Constant.MAP_LIST_FREQUENT_PLACE_SIZE));
	}

	@Override
	public List<String> getSuggestPlaceName(String pattern) {
		
		return mapServiceHelper.getSuggestionCellsName(pattern, Integer.parseInt(Constant.MAP_LIST_SUGGESTION_SIZE));
	}

	@Override
	public void addToSearchHistory(int cellId) {
		SharedPreferences sp = activity.getSharedPreferences(
				Constant.SHARED_PREFERENCE_MAP_HISTORY_FILE,
				Context.MODE_PRIVATE);
		String cellIdString = sp.getString(
				Constant.SHARED_PREFERENCE_MAP_HSITORY_KEY, "");
		
		if(cellIdString.equals("")) {
			cellIdString = String.valueOf(cellId);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(Constant.SHARED_PREFERENCE_MAP_HSITORY_KEY,
					cellIdString);
			editor.commit();
			return ;
		}
		
		String[] id = cellIdString.split("#");

		int newCellIdStringLength = id.length;
		boolean cellIdInId = false;// cellId is in id set
		for (int i = 0; i < id.length; i++) {
			if (Integer.parseInt(id[i]) == cellId) {
				int j = i - 1;
				for (; j >= 0; j--) {
					id[j + 1] = id[j];
				}
				id[0] = String.valueOf(cellId);
				newCellIdStringLength = id.length;
				cellIdInId = true;
				break;
			}
		}
		// cellId is not in id set,add it to set ,the size of set is at most
		// Constant.MAP_LIST_HISTORY_SIZE
		if (cellIdInId == false) {
			int n = Integer.parseInt(Constant.MAP_LIST_HISTORY_SIZE);
			if (n == id.length) {
				for (int i = n - 1; i > 0; i--) {
					id[i] = id[i - 1];
				}
				id[0] = String.valueOf(cellId);
				newCellIdStringLength = id.length;
			} else {
				for (int i = id.length; i > 0; i--) {
					id[i] = id[i - 1];
				}
				id[0] = String.valueOf(cellId);
				newCellIdStringLength = id.length + 1;
			}
		}

		String newCellIdString = id[0];
		for (int i = 1; i < newCellIdStringLength; i++) {
			newCellIdString = newCellIdString + "#" + id[i];
		}

		Log.i(TAG, "in addTOSearchHistory[newCellIdString]:" + newCellIdString);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constant.SHARED_PREFERENCE_MAP_HSITORY_KEY,
				newCellIdString);
		editor.commit();

	}

	@Override
	public Building getBuildingById(int id) {
		return mapServiceHelper.getBuildingById(id);
	}

	@Override
	public Cell getCellById(int id) {
		return mapServiceHelper.getCellById(id);
	}

	/**
	 * @param id
	 *            is the path dot it
	 * @return a pathdot
	 */
	@Override
	public PathDot getPathDot(int id) {
		return mapServiceHelper.getPathDotById(id);
	}

	@Override
	public String getShortestPath(long sourceId, long destId) {
		return mapServiceHelper.getShortestPath(sourceId, destId);
	}

}
