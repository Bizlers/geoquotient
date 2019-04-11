package com.bizlers.geoq.discovery.config;

import com.jcabi.aspects.Loggable;
import com.jcabi.log.Logger;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfiguration {

	private static final int MAX_CONNECTION_IDLE_TIME = 60000;

	@Value("${mongodb.uri}")
	private String uri;

	@Value("${mongodb.database}")
	private String dbname;

	@Bean
	@Qualifier("geoq")
	@Loggable()
	public MongoClient mongoClient() {
		Logger.debug(this, "Connecting to uri: %s", uri);
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.maxConnectionIdleTime(MAX_CONNECTION_IDLE_TIME);
		MongoClientURI mongoClientURI = new MongoClientURI(uri, builder);
		return new MongoClient(mongoClientURI);
	}

	@Bean
	@Qualifier("geoq")
	@Loggable()
	public Datastore datastore() {
		Logger.debug(this, "Creating datastore");
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.bizlers.geoq.discovery.model");
		Datastore datastore = morphia.createDatastore(mongoClient(), dbname);
		datastore.ensureIndexes();
		return datastore;
	}
}
