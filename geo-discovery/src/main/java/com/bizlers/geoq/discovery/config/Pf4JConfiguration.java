package com.bizlers.geoq.discovery.config;

import com.bizlers.geoq.discovery.DatasetPreloader;
import com.jcabi.log.Logger;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
@PropertySource("classpath:application.properties")
public class Pf4JConfiguration {

	private static final String SEP = File.separator;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${pf4j.pluginsDir:#{null}}")
	private String pluginsDir;

	@Bean
	public SpringPluginManager pluginManager() throws IOException, URISyntaxException {
		File pluginsRoot;
		if (pluginsDir != null) {
			pluginsRoot = new File(pluginsDir);
			Logger.debug(this, "Using pluginRoot from application.properties");
		} else {
			Resource resource = resourceLoader.getResource("classpath:plugins");
			if (resource.exists() && resource.isFile()) {
				pluginsRoot = resource.getFile();
				Logger.debug(this, "Using pluginRoot from classpath");
			} else {
				String userDir = System.getProperty("user.dir");
				Logger.debug(this, "user.dir = %s", userDir);
				File exeRoot = new File(userDir).getParentFile();
				if (exeRoot == null || !exeRoot.exists()) {
					// Since user.dir doesn't work in docker environment
					String jarPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation()
							.toURI()).getPath();
					Logger.debug(this, "Using runnable jar path: %s", jarPath);
					exeRoot = new File(jarPath).getParentFile().getParentFile();
				}
				pluginsRoot = new File(exeRoot + SEP + "lib" + SEP + "plugins");
				Logger.debug(this, "Using pluginRoot from: %s", pluginsRoot);
			}
			Logger.debug(this, "pluginRoot: " + resource);
		}
		if (pluginsRoot.exists() && pluginsRoot.isDirectory()) {
			return new SpringPluginManager(pluginsRoot.toPath());
		} else {
			Logger.warn(this, "plugins directory '%s' does not exist.", pluginsRoot);
		}
		return new SpringPluginManager();
	}

	@Bean
	@DependsOn("pluginManager")
	public DatasetPreloader datasetPreloader() {
		return new DatasetPreloader();
	}
}