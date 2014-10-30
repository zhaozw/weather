package com.young.modules.location;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class CityLocation {

	private LocationManager locationManager;
	CityLocation location;
	Context mContext;
	private String networkProvider = LocationManager.NETWORK_PROVIDER;// ͨ��network��ȡlocation

	// private String GpsProvider =
	// LocationManager.GPS_PROVIDER;//ͨ��gps��ȡlocation
	

	public CityLocation(Context mContext) {
		this.mContext = mContext;
		initLocation(mContext);
	}

	
	private void initLocation(Context mContext) {
		locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// String provider = locationManager.getBestProvider(criteria, true);

		Location location = locationManager
				.getLastKnownLocation(networkProvider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(networkProvider, 2000, 10,
				locationListener);
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) {
		String latLongString;
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "γ��:" + lat + "\n����:" + lng;
		} else {
			latLongString = "�޷���ȡ������Ϣ";
		}
		List<Address> addList = null;
		Geocoder ge = new Geocoder(mContext);
		try {
			addList = ge.getFromLocation(24.463, 118.1, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (addList != null && addList.size() > 0) {
			for (int i = 0; i < addList.size(); i++) {
				Address ad = addList.get(i);
				latLongString += "\n";
				latLongString += ad.getCountryName() + ";" + ad.getLocality();
			}
		}
		System.out.println("��ǰ��λ����:\n" + latLongString);
	}

}
