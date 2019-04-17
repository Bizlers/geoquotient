package com.bizlers.geoq.discovery.service;

import com.bizlers.geoq.discovery.dao.ResourceDao;
import com.bizlers.geoq.discovery.model.Resource;
import com.bizlers.geoquotient.utils.GeoLocation;
import com.jcabi.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Class which manages objects on update requests by client
 * 
 * @author Pawan D
 * 
 */
@Service
public class UpdateDirector {

	@Autowired
	private ResourceLocationRepository resourceLocationRepository;

	@Autowired
	private EntityTileProvider entityTileProvider;

	@Autowired
	private ResourceDao resourceDao;

	public GeoLocation getLocation(Object resourceId) {
		return resourceLocationRepository.getLocation(resourceId);
	}

	public void updateLocation(String resourceId, GeoLocation newGeoLocation) {
		Logger.debug(this, "Resource '%s' has updated location: %s", resourceId, newGeoLocation);
		// Know the older geoLocation and old entity tile before we change
		// the resource location
		GeoLocation oldGeoLocation = null;
		Resource resource = resourceDao.find(resourceId);
		if (resource == null) {
			resource = new Resource(resourceId, newGeoLocation);
			resourceDao.create(resource);
		} else {
			oldGeoLocation = resourceLocationRepository.getLocation(resourceId);
			resource.setGeoLocation(newGeoLocation);
			resourceDao.update(resource);
		}
		if (resourceLocationRepository.contains(resourceId)) {
			oldGeoLocation = resourceLocationRepository.getLocation(resourceId);
		}

		if (newGeoLocation == null) {
			resourceLocationRepository.removeResource(resourceId);
		} else {
			return;
		}

		EntityTile oldEntityTile = entityTileProvider.getEntityTile(oldGeoLocation);
		EntityTile activeEntityTile = entityTileProvider.getEntityTile(newGeoLocation);

		if (activeEntityTile != null) {
			if (activeEntityTile.contains(resourceId)) {
				activeEntityTile.resourceMoves(resourceId);
			} else {
				if (oldEntityTile != null) {
					oldEntityTile.resourceLeaves(resourceId);
				}
				activeEntityTile.resourceEnters(resourceId);
			}
		} else {
			if (oldEntityTile != null)
				oldEntityTile.resourceLeaves(resourceId);
		}
	}

	public Set<Resource> getNearby(double latitude, double longitude, int radius) {
		return resourceLocationRepository.getNearbyResourcesFor(new GeoLocation(latitude, longitude)).keySet();
	}
}