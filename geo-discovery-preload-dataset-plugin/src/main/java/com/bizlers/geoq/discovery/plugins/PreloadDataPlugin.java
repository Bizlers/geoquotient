package com.bizlers.geoq.discovery.plugins;

import com.bizlers.geoq.discovery.plugin.GeoDiscoveryServiceInitializer;
import com.bizlers.geoq.discovery.plugin.ResourceLocationUpdater;
import org.apache.commons.lang.StringUtils;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

public class PreloadDataPlugin extends Plugin {

	public PreloadDataPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
		System.out.println("PreloadDataPlugin.start()");
		// for testing the development mode
		if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
			System.out.println(StringUtils.upperCase("PreloadDataPlugin"));
		}
	}

	@Override
	public void stop() {
		System.out.println("PreloadDataPlugin.stop()");
	}

	@Extension
	public static class PreloadServiceData implements GeoDiscoveryServiceInitializer {

		@Override
		public void initialize(ResourceLocationUpdater resourceLocationUpdater) {

		}
	}
}
