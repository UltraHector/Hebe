package com.TroyEmpire.Hebe.IServices;

import com.TroyEmpire.Hebe.Entities.LocationPosition;

import android.location.Location;

public interface IGpsService {
	public double getCurrentLatitute();
	public double getCurrentLongitude();
	public Location getCurrentLocation();
	public LocationPosition  getLocationPosition(); 
	public String getProvider();
}
