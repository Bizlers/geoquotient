package com.bizlers.geoq.discovery;

import com.bizlers.geoq.discovery.config.EmbeddedTomcatConfig;
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

		// retrieves automatically the extensions for the Greeting.class extension point
		Greetings greetings = applicationContext.getBean(Greetings.class);
		greetings.printGreetings();

		// stop plugins
		PluginManager pluginManager = applicationContext.getBean(PluginManager.class);
        /*
        // retrieves manually the extensions for the Greeting.class extension point
        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);
        System.out.println("greetings.size() = " + greetings.size());
        */
		pluginManager.stopPlugins();
	}
}
