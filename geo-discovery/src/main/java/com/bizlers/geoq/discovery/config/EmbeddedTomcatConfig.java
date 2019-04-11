package com.bizlers.geoq.discovery.config;

import com.jcabi.log.Logger;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedTomcatConfig {

	private static final String FORMAT_JKS = "JKS";

	@Value("${server.ssl.enabled}")
	private boolean sslEnabled;

	@Value("${server.port}")
	private String serverPort;

	@Value("${server.session.port:#{null}}")
	private String sessionPort;

	@Value("${server.ssl.key-store:#{systemProperties['user.home']}/.turbo/certificates/keystore.jks}")
	private String keyStoreFile;

	@Value("${server.ssl.key-store-password}")
	private String keystorePassword;

	@Value("${server.ssl.key-password}")
	private String keyPassword;

	@Value("${server.ssl.key-alias}")
	private String keystoreAlias;

	@Autowired
	private ServerProperties serverProperties;

	@Bean
	@ConditionalOnProperty(name="server.ssl.enabled", havingValue="true")
	public TomcatServletWebServerFactory servletContainerFactory(Connector connector, Ssl ssl) {
		Logger.info(this, "Creating new EmbeddedServletContainerFactory");
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(connector);
		serverProperties.setSsl(ssl);
		return tomcat;
	}

	@Bean
	public Connector connector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setSecure(true);
		connector.setScheme("https");
		connector.setPort(Integer.valueOf(sessionPort));

		connector.setAttribute("SSLEnabled", true);
		connector.setAttribute("sslProtocol", "TLS");
		connector.setAttribute("clientAuth", false);
		connector.setAttribute("keystoreFile", keyStoreFile);
		connector.setAttribute("keystoreType", FORMAT_JKS);
		connector.setAttribute("keystorePass", keystorePassword);
		connector.setAttribute("keystoreAlias", keystoreAlias);
		connector.setAttribute("keyPass", keystorePassword);
		return connector;
	}

	@Bean
	public Ssl ssl() {
		Ssl ssl = new Ssl();
		ssl.setEnabled(true);
		ssl.setProtocol("TLS");
		ssl.setClientAuth(Ssl.ClientAuth.WANT);
		ssl.setKeyStore(keyStoreFile);
		ssl.setKeyStoreType(FORMAT_JKS);
		ssl.setKeyStorePassword(keystorePassword);
		ssl.setKeyPassword(keyPassword);
		ssl.setKeyAlias(keystoreAlias);
		return ssl;
	}
}
