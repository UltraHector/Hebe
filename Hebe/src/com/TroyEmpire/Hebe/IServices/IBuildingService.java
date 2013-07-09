package com.TroyEmpire.Hebe.IServices;

import com.TroyEmpire.Hebe.Entities.Building;
import com.TroyEmpire.Hebe.Entities.LocationPosition;

public interface IBuildingService {
	
	/**
	 * @param cellName is the name of a in a smaller place building
	 */
	public Building getBuildingByName(String cellName);
	
	/**
	 * @param latitude the input latitude click by the user
	 * @param longitude the input longitude click by the user
	 */
	public Building getBuildingByLocation(double latitude, double longitude);
	
}
