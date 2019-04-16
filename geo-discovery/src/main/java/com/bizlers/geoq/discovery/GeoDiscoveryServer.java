package com.bizlers.geoq.discovery;

import com.bizlers.geoq.discovery.config.EmbeddedTomcatConfig;
import com.bizlers.geoq.discovery.service.ResourceLocationRepository;
import org.pf4j.PluginManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(EmbeddedTomcatConfig.class)
public class GeoDiscoveryServer {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(GeoDiscoveryServer.class, args);

		DatasetPreloader dataLoader = applicationContext.getBean(DatasetPreloader.class);
		ResourceLocationRepository locationRepository = applicationContext.getBean(ResourceLocationRepository.class);
		dataLoader.preload(locationRepository);

		// stop plugins
		PluginManager pluginManager = applicationContext.getBean(PluginManager.class);
		pluginManager.stopPlugins();
	}
}
