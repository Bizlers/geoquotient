package com.bizlers.geoquotient.utils;

import java.io.Serializable;

public class GeoLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private double latitude;

	private double longitude;

	private int accuracy;
	
	private long time;

	public static final int ACCURACY_HIGH = 0;

	public static final int ACCURACY_LOW = 1;

	public GeoLocation() {
		// Default constructor; Used by json deserialization APIs
	}

	public GeoLocation(double latitude, double longitude, int accuracy) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.accuracy = accuracy;
	}

	public GeoLocation(double latitude, double longitude) {
		this(latitude, longitude, ACCURACY_HIGH);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return latitude + "," + longitude;
	}

	@Override
	public boolean equals(Object geoLocation) {
		if (geoLocation != null && geoLocation instanceof GeoLocation) {
			return (((GeoLocation) geoLocation).getLatitude() == latitude)
					&& (((GeoLocation) geoLocation).getLongitude() == longitude);
		}
		return false;
	}
}
