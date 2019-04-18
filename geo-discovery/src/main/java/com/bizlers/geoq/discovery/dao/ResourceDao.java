package com.bizlers.geoq.discovery.dao;

import com.bizlers.geoq.models.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceDao extends BaseMongoDao<Object, Resource> {

	public ResourceDao() {
		super(Resource.class);
	}
}
