package com.bizlers.geoq.discovery.model;

import com.bizlers.geoquotient.utils.GeoLocation;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

@Data
@ApiModel
@Entity("resources")
public class Resource implements Serializable {

	@Id
	private String resourceId;

	private GeoLocation geoLocation;

	public Resource() {

	}

	public Resource(String resourceId, GeoLocation geoLocation) {
		this.resourceId = resourceId;
		this.geoLocation = geoLocation;
	}
}
