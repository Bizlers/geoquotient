package com.bizlers.geoq.discovery.model;

import com.bizlers.geoquotient.utils.GeoLocation;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class Resource implements Serializable {

	private Object resourceId;

	private GeoLocation geoLocation;

	public Resource(Object resourceId, GeoLocation geoLocation) {
		this.resourceId = resourceId;
		this.geoLocation = geoLocation;
	}
}
