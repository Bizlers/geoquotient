package com.bizlers.geoq.discovery;

import com.bizlers.geoq.discovery.dao.ResourceDao;
import com.bizlers.geoq.discovery.model.Resource;
import com.bizlers.geoq.discovery.plugin.GeoDiscoveryServiceInitializer;
import com.bizlers.geoq.discovery.service.ResourceLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class DatasetPreloader {

	@Autowired
	private GeoDiscoveryServiceInitializer serviceInitializer;

	@Autowired
	ResourceDao resourceDao;

	public void preload(ResourceLocationRepository resourceLocationRepository) {
		Collection<Resource> resources = resourceDao.findAll();
		for (Resource res:resources) {
			resourceLocationRepository.updateResource(res.getResourceId(), res.getGeoLocation());
		}
		serviceInitializer.initialize(resourceLocationRepository);
	}

}