package com.bizlers.geoq.discovery.plugin;

import org.pf4j.ExtensionPoint;

public interface GeoDiscoveryServiceInitializer extends ExtensionPoint {

	void initialize(ResourceLocationUpdater resourceLocationUpdater);
}