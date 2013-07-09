package com.TroyEmpire.Hebe.IServices;

import java.util.List;

import com.TroyEmpire.Hebe.Entities.Building;
import com.TroyEmpire.Hebe.Entities.Cell;
import com.TroyEmpire.Hebe.Entities.PathDot;

public interface IMapService {
	public void addToSearchHistory(int cellId);
	public List<Cell> getSearchHistory();
	
	/**
	 * @param n is n place order by hitcount
	 * @return a list of cell  
	 * */
	public List<Cell> getFrequentPlace();
	public List<String> getSuggestPlaceName(String pattern);
	
	
	
	
	public Building getBuildingById(int id);
	public Cell getCellById(int id);
	
	/**
	 * @param id of pathDot
	 * @return pathDot
	 * */
	public PathDot getPathDot(int id);
	
	/**
	 * @param id of sourcePathDot , id of destPathDot
	 * @return a String of id which consist a shortest path.
	 * */
	public String getShortestPath(long sourceId, long destId);
	
}	
