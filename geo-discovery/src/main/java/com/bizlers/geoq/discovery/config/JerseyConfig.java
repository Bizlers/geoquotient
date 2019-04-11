package com.bizlers.geoq.discovery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ApplicationPath("/geoq/gds/v1")
public class JerseyConfig extends ResourceConfig {

	private static final Logger log = Logger.getLogger(JerseyConfig.class.getName());

	@Autowired
	public JerseyConfig(ObjectMapper objectMapper) {
		// register endpoints
		packages("com.bizlers.geoq.discovery.server.controller", "com.bizlers.auth.commons");
		// register jackson for json
		register(new ObjectMapperContextResolver(objectMapper));
		register(MultiPartFeature.class);
		register(new LoggingFeature(log, Level.FINE, LoggingFeature.Verbosity.PAYLOAD_ANY, 1000));
	}

	@Provider
	public static class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

		private final ObjectMapper mapper;

		public ObjectMapperContextResolver(ObjectMapper mapper) {
			this.mapper = mapper;
		}

		@Override
		public ObjectMapper getContext(Class<?> type) {
			return mapper;
		}
	}
}