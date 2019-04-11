package com.bizlers.geoq.discovery.config;

import com.bizlers.geoq.discovery.Greetings;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Configuration
public class SpringConfiguration {

	@Autowired
	ResourceLoader resourceLoader;

	@Bean
	public SpringPluginManager pluginManager() throws IOException {
		File pluginsRoot = ResourceUtils.getFile("classpath:plugins");
		if (pluginsRoot.exists() && pluginsRoot.isDirectory()) {
			return new SpringPluginManager(pluginsRoot.toPath());
		}
		return new SpringPluginManager();
	}

	@Bean
	@DependsOn("pluginManager")
	public Greetings greetings() {
		return new Greetings();
	}

}