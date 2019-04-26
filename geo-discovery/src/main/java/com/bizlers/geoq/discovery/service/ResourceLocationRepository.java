package com.bizlers.geoq.discovery.service;

import com.bizlers.geoq.discovery.dao.ResourceDao;
import com.bizlers.geoq.models.Resource;
import com.bizlers.geoquotient.utils.GeoCalculator;
import com.bizlers.geoquotient.utils.GeoLocation;
import com.jcabi.aspects.Loggable;
import com.jcabi.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@PropertySource("classpath:geo-discovery.properties")
public class ResourceLocationRepository {

	@Autowired
	private EntityTileProvider entityTileProvider;

	@Autowired
	private ResourceDao resourceDao;

	@Value("${search_radius}")
	private int searchRadius;

	@Value("${accuracy_limiting_factor}")
	private float accuracyLimitingFactor;

	@Value("${maximum_search_radius}")
	private int maxSearchRadius;

	@Value("${strong_accuracy}")
	private int strongAccuracy;

	private final Map<Object, GeoLocation> resourceLocations = new ConcurrentHashMap<>();

	@Loggable(Loggable.TRACE)
	boolean contains(Object id) {
		return resourceLocations.containsKey(id);
	}

	@Loggable(Loggable.TRACE)
	public void updateResource(Object id, GeoLocation geoLocation) {
		resourceLocations.put(id, geoLocation);
	}

	@Loggable(Loggable.TRACE)
	void removeResource(Object id) throws IllegalArgumentException {
		resourceLocations.remove(id);
	}

	@Loggable(Loggable.TRACE)
	GeoLocation getLocation(Object id) {
		GeoLocation geoLocation = null;
		if (contains(id)) {
			geoLocation = resourceLocations.get(id);
		}
		return geoLocation;
	}

	@Loggable(Loggable.DEBUG)
	public Map<Resource, Visibility> getNearbyResourcesFor(Object resourceId) {
		GeoLocation geoLocation = getLocation(resourceId);
		Map<Resource, Visibility> nearbyResources = getNearbyResourcesFor(geoLocation);
		nearbyResources.remove(resourceId);
		return nearbyResources;
	}

	@Loggable(Loggable.DEBUG)
	Map<Resource, Visibility> getNearbyResourcesFor(GeoLocation geoLocation) {
		Map<Resource, Visibility> nearbyResources = new HashMap<>();
		if (geoLocation != null) {
			EntityTile entityTile = entityTileProvider.getEntityTile(geoLocation);
			// first add resources from current tile
			Logger.debug(this, "Searching for nearby resources in tile %s for location %s", entityTile, geoLocation);
			synchronized (entityTile) {
				if (entityTile.getResourcesCount() > 0) {
					for (Object resourceId : entityTile.getResourceIds()) {
						Visibility v = getResourceVicinity(geoLocation, resourceId);
						if (v != Visibility.NO_VISIBILITY) {
							Resource resource = resourceDao.find(resourceId);
							nearbyResources.put(resource, v);
						}
					}
				}
			}
			EntityTile[] surroundingTiles = entityTileProvider.getSurroundingTiles(entityTile);
			Logger.debug(this, "Searching for nearby resources in tiles %[list]s for location %s", surroundingTiles, geoLocation);
			for (EntityTile eTile : surroundingTiles) {
				synchronized (eTile) {
					if (eTile.getResourcesCount() > 0) {
						List<Object> resourcesFromTile = eTile.getResourceIds();
						for (Object resourceId : resourcesFromTile) {
							Visibility v = getResourceVicinity(geoLocation, resourceId);
							if (v != Visibility.NO_VISIBILITY) {
								Resource resource = resourceDao.find(resourceId);
								nearbyResources.put(resource, v);
							}
						}
					}
				}
			}
		}
		return nearbyResources;
	}

	private Visibility getResourceVicinity(GeoLocation srcLocation, Object resourceId) {
		GeoLocation dstLocation = getLocation(resourceId);
		if (dstLocation != null) {
			int srcAccuracy = (int) (srcLocation.getAccuracy() * accuracyLimitingFactor);
			int dstAccuracy = (int) (dstLocation.getAccuracy() * accuracyLimitingFactor);
			int searchRadius = Math.min((this.searchRadius + srcAccuracy), maxSearchRadius);
			int distance = (int) GeoCalculator.distanceInMetersFrom(
					srcLocation, dstLocation, GeoCalculator.ACCURACY_LOW);
			if (distance < this.searchRadius) {
				if (srcAccuracy + dstAccuracy + distance < this.searchRadius) {
					return Visibility.VISIBILITY_HIGH;
				} else {
					return Visibility.VISIBILITY_LOW;
				}
			} else if (distance < searchRadius) {
				return Visibility.VISIBILITY_LOW;
			} else if (distance < searchRadius + strongAccuracy) {
				return Visibility.VISIBILITY_LOW;
			}
		}
		return Visibility.NO_VISIBILITY;
	}

	@Loggable(Loggable.DEBUG)
	public boolean isResourceNearby(Object resourceId, GeoLocation targetLocation) {
		GeoLocation srcLocation = getLocation(resourceId);
		return locationsNearby(srcLocation, targetLocation);
	}

	@Loggable(Loggable.DEBUG)
	private boolean locationsNearby(GeoLocation srcLocation, GeoLocation targetLocation) {
		if (srcLocation != null && targetLocation != null) {
			int distance = (int) GeoCalculator.distanceInMetersFrom(srcLocation, targetLocation, GeoCalculator.ACCURACY_LOW);
			return distance <= searchRadius;
		}
		return false;
	}
}