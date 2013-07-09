package com.TroyEmpire.Hebe.Services;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.TroyEmpire.Hebe.Entities.LocationPosition;
import com.TroyEmpire.Hebe.IServices.IGpsService;

public class GpsService implements IGpsService, LocationListener {

	private LocationManager locationManager;
	private Location location;
	private Criteria criteria;
	private String provider;

	public GpsService(LocationManager locationManager) {
		this.locationManager = locationManager;
		this.criteria = new Criteria();
		this.criteria.setAccuracy(Criteria.ACCURACY_FINE); // Use a finer
															// accuracy
		provider = locationManager.getBestProvider(this.criteria,
				true);
		if (provider != null)
			this.location = locationManager.getLastKnownLocation(provider);
		else
			this.location = null;
	}
	
	public String getProvider(){
		return provider;
	}

	@Override
	public double getCurrentLatitute() {
		if (this.location != null)
			return location.getLatitude();
		else
			return 0;
	}

	@Override
	public double getCurrentLongitude() {
		if (this.location != null)
			return location.getLongitude();
		else
			return 0;
	}

	@Override
	public Location getCurrentLocation() {
		return this.location;
	}

	@Override
	public LocationPosition getLocationPosition() {
		LocationPosition position = new LocationPosition();
		if (this.location != null) {
			position.setLatitude(location.getLatitude());
			position.setLongitude(location.getLongitude());
		} else {
			position.setLongitude(0);
			position.setLongitude(0);
		}
		return position;
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderDisabled(String provider) {		
	}

	@Override
	public void onProviderEnabled(String provider) {
	}
}
