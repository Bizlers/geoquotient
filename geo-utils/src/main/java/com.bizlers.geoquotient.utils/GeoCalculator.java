package com.bizlers.geoquotient.utils;

public class GeoCalculator {

	/**
	 * Used to specify the method used for distance calculation.
	 * <p>
	 * Use ACCURACY_LOW when you don't need accurate distace.
	 */
	public static final int ACCURACY_LOW = 0;

	/**
	 * Used to specify the method used for distance calculation.
	 * <p>
	 * Use ACCURACY_HIGH when you don't need accurate distace.
	 */
	public static final int ACCURACY_HIGH = 1;

	/**
	 * This method computes distance between two GeoLocations using Vincenty
	 * inverse formula for ellipsoids
	 * 
	 * <p>
	 * This is slower but more accurate method
	 * 
	 * @param geoLocation1
	 *            The start geo-location
	 * @param geoLocation2
	 *            The end geo-location
	 * @return distance in meters between points with 5.10<sup>-4</sup>
	 *         precision
	 * @see <a
	 *      href="http://www.movable-type.co.uk/scripts/latlong-vincenty.html">Originally
	 *      posted here</a>
	 */
	public static double distanceVincenty(GeoLocation geoLocation1,
			GeoLocation geoLocation2) {
		final double lat1 = geoLocation1.getLatitude();
		final double lat2 = geoLocation2.getLatitude();
		final double lon1 = geoLocation1.getLongitude();
		final double lon2 = geoLocation2.getLongitude();

		final double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84
		// ellipsoid
		// params
		double L = Math.toRadians(lon2 - lon1);
		double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
		double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
		double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

		double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
		double lambda = L, lambdaP, iterLimit = 100;
		do {
			sinLambda = Math.sin(lambda);
			cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
					* (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
			if (sinSigma == 0)
				return 0; // co-incident points
			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
			if (Double.isNaN(cos2SigmaM))
				cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (�6)
			double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
			lambdaP = lambda;
			lambda = L
					+ (1 - C)
					* f
					* sinAlpha
					* (sigma + C
							* sinSigma
							* (cos2SigmaM + C * cosSigma
									* (-1 + 2 * cos2SigmaM * cos2SigmaM)));
		} while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

		if (iterLimit == 0)
			return Double.NaN; // formula failed to converge

		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		double A = 1 + uSq / 16384
				* (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
		double deltaSigma = B
				* sinSigma
				* (cos2SigmaM + B
						/ 4
						* (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B
								/ 6 * cos2SigmaM
								* (-3 + 4 * sinSigma * sinSigma)
								* (-3 + 4 * cos2SigmaM * cos2SigmaM)));
		double dist = b * A * (sigma - deltaSigma);

		return dist;
	}

	/**
	 * This method uses Haversine formula to compute distance between two
	 * GeoLocations.
	 * <p>
	 * This is faster but less accurate method
	 * 
	 * @param geoLocation1
	 *            The start geo-location
	 * @param geoLocation2
	 *            The end geo-location
	 * @return distance in meters
	 */
	public static double distanceHaversine(GeoLocation geoLocation1,
			GeoLocation geoLocation2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(geoLocation2.getLatitude()
				- geoLocation1.getLatitude());
		double dLng = Math.toRadians(geoLocation2.getLongitude()
				- geoLocation1.getLongitude());
		double a = Math.pow(Math.sin(dLat / 2), 2)
				+ Math.cos(Math.toRadians(geoLocation1.getLatitude()))
				* Math.cos(Math.toRadians(geoLocation2.getLatitude()))
				* Math.pow(Math.sin(dLng / 2), 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (dist * meterConversion);
	}

	/**
	 * This computes and returns distance between two geo-locations.
	 * 
	 * @param geoLocation1
	 *            The source geo-location
	 * @param geoLocation2
	 *            The target geo-location
	 * @param accuracy
	 *            The accuracy of the GeoLocation
	 * @return distance in meters
	 */
	public static double distanceInMetersFrom(GeoLocation geoLocation1,
			GeoLocation geoLocation2, int accuracy) {
		if (!geoLocation1.equals(geoLocation2)) {
			if (accuracy <= GeoCalculator.ACCURACY_LOW) {
				return distanceHaversine(geoLocation1, geoLocation2);
			} else { // if(accuracy >= GeoCalculator.ACCURACY_HIGH) {
				return distanceVincenty(geoLocation1, geoLocation2);
			}
		}
		return 0;
	}
}
