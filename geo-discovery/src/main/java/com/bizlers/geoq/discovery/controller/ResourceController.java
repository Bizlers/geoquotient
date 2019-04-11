package com.bizlers.geoq.discovery.controller;

import com.bizlers.geoq.discovery.model.Resource;
import com.bizlers.geoq.discovery.service.UpdateDirector;
import com.bizlers.geoquotient.utils.GeoLocation;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Api
@Component
@Path("/resources")
public class ResourceController {

	@Autowired
	private UpdateDirector updateDirector;

	@PUT
	@Path("/{resourceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateLocation(@PathParam("resourceId") String resourceId, GeoLocation geoLocation) {
		updateDirector.updateLocation(resourceId, geoLocation);
	}

	@GET
	@Path("/{resourceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@PathParam("resourceId") String resourceId) {
		GeoLocation geoLocation = updateDirector.getLocation(resourceId);
		return Response.ok(geoLocation).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNearby(@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude, @QueryParam("radius") int radius) {
		Set<Resource> resources = updateDirector.getNearby(latitude, longitude, radius);
		return Response.ok(resources).build();
	}
}
