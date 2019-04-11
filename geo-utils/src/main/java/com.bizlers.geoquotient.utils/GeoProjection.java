package com.bizlers.geoquotient.utils;

public class GeoProjection {

	public static final int TILE_SIZE_REGULAR = 256;

	public static final int ZOOM_LEVEL_DEFAULT = 20;

	public static final GeoProjection DEFAULT = new GeoProjection(
			ZOOM_LEVEL_DEFAULT);

	public int zoomLevel;
	private double degreesToRadiansRatio = 180 / Math.PI;
	private double radiansToDegreesRatio = Math.PI / 180d;
	private Point pixelGlobeCenter;
	private double xPixelsToDegreesRatio;
	private double yPixelsToRadiansRatio;

	public GeoProjection(int zoomLevel) {
		this.zoomLevel = zoomLevel;

		double pixelGlobeSize = TILE_SIZE_REGULAR * Math.pow(2, zoomLevel);
		xPixelsToDegreesRatio = pixelGlobeSize / 360;
		yPixelsToRadiansRatio = pixelGlobeSize / (2 * Math.PI);

		long halfPixelGlobeSize = (long) pixelGlobeSize / 2;
		pixelGlobeCenter = new Point(halfPixelGlobeSize, halfPixelGlobeSize);
	}

	public Point getPointFromGeoLocation(GeoLocation location) {
		Point p = null;
		if (location != null) {
			double x = Math.round(pixelGlobeCenter.x
					+ (location.getLongitude() * xPixelsToDegreesRatio));
			double f = Math.min(
					Math.max(
							Math.sin(location.getLatitude()
									* radiansToDegreesRatio), -0.9999d),
					0.9999d);
			double y = Math.round(pixelGlobeCenter.y + .5d
					* Math.log((1d + f) / (1d - f)) * -yPixelsToRadiansRatio);
			p = new Point((long) x, (long) y);
		}
		return p;

	}

	public GeoLocation getGeoLocationFromPoint(Point point) {
		double longitude = (point.x - pixelGlobeCenter.x)
				/ xPixelsToDegreesRatio;
		double latitude = (2 * Math.atan(Math
				.exp((point.y - pixelGlobeCenter.y) / -yPixelsToRadiansRatio)) - Math.PI / 2)
				* degreesToRadiansRatio;
		return new GeoLocation(latitude, longitude);
	}

	public Point getTileXY(GeoLocation geoLocation) {
		return getTileXY(getPointFromGeoLocation(geoLocation));
	}

	protected Point getTileXY(Point point) {
		return new Point((long) Math.floor(point.x / 256),
				(long) Math.floor(point.y / 256));
	}
}