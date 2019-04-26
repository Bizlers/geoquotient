package com.bizlers.geoq.discovery.controller;

import com.bizlers.geoq.discovery.service.DiscoveryService;
import com.bizlers.geoq.models.Resource;
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
	private DiscoveryService discoveryService;

	@PUT
	@Path("/{resourceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateLocation(@PathParam("resourceId") String resourceId, GeoLocation geoLocation) {
		discoveryService.updateLocation(resourceId, geoLocation);
		return Response.ok("Location updated").build();
	}

	@GET
	@Path("/{resourceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@PathParam("resourceId") String resourceId) {
		GeoLocation geoLocation = discoveryService.getLocation(resourceId);
		return Response.ok(geoLocation).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNearby(@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude, @QueryParam("radius") int radius) {
		Set<Resource> resources = discoveryService.getNearby(latitude, longitude, radius);
		return Response.ok(resources).build();
	}
}
