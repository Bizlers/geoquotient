package com.bizlers.geoq.discovery;

import com.bizlers.geoq.discovery.dao.ResourceDao;
import com.bizlers.geoq.discovery.service.DiscoveryService;
import com.bizlers.geoq.models.Resource;
import com.bizlers.geoq.discovery.plugin.GeoDiscoveryServiceInitializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class DatasetPreloader {

	@Autowired
	private GeoDiscoveryServiceInitializer serviceInitializer;

	@Autowired
	private ResourceDao resourceDao;

	void preload(DiscoveryService discoveryService) {
		Collection<Resource> resources = resourceDao.findAll();
		for (Resource res:resources) {
			discoveryService.updateResource(res.getResourceId(), res.getGeoLocation());
		}
		serviceInitializer.initialize(discoveryService);
	}

}