package com.bizlers.geoq.discovery.plugin;

import com.bizlers.geoquotient.utils.GeoLocation;

public interface ResourceLocationUpdater {

	void updateResource(Object resourceId, GeoLocation geoLocation);
}
