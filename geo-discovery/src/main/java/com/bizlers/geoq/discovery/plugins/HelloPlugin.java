package com.bizlers.geoq.discovery.plugins;

import com.bizlers.geoq.discovery.Greeting;
import org.apache.commons.lang.StringUtils;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;

public class HelloPlugin extends Plugin {

	public HelloPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
		System.out.println("WelcomePlugin.start()");
		// for testing the development mode
		if (RuntimeMode.DEVELOPMENT.equals(wrapper.getRuntimeMode())) {
			System.out.println(StringUtils.upperCase("WelcomePlugin"));
		}
	}

	@Override
	public void stop() {
		System.out.println("WelcomePlugin.stop()");
	}

	@Extension
	public static class HelloGreeting implements Greeting {

		@Override
		public String getGreeting() {
			return "Welcome";
		}

	}
}
