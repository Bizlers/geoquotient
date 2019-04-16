package com.bizlers.geoq.discovery.plugins.hello;

import com.bizlers.geoq.discovery.plugin.GeoDiscoveryServiceInitializer;
import com.bizlers.geoq.discovery.plugin.ResourceLocationUpdater;
import org.apache.commons.lang.StringUtils;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

public class PreloadServiceDataPlugin extends Plugin {

	public PreloadServiceDataPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
		System.out.println("PreloadServiceDataPlugin.start()");
		// for testing the development mode
		if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
			System.out.println(StringUtils.upperCase("PreloadServiceDataPlugin"));
		}
	}

	@Override
	public void stop() {
		System.out.println("PreloadServiceDataPlugin.stop()");
	}

	@Extension
	public static class PreloadServiceData implements GeoDiscoveryServiceInitializer {

		@Override
		public void initialize(ResourceLocationUpdater resourceLocationUpdater) {

		}
	}
}
