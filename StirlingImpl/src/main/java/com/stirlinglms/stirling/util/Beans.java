package com.stirlinglms.stirling.util;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
public class Beans {

    private static final String SECURITY_USER = "CONFIDENTIAL";
    private final static String REDIRECT_PATTERN = "/*";

    private static final String CONNECTOR_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private final static String CONNECTOR_SCHEME = "http";

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory factory) {
        return new RedisTokenStore(factory);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                SecurityCollection collection = new SecurityCollection();

                constraint.setUserConstraint(SECURITY_USER);
                collection.addPattern(REDIRECT_PATTERN);

                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };

        factory.addAdditionalTomcatConnectors(this.getConnector());

        return factory;
    }

    private Connector getConnector() {
        Connector connector = new Connector(CONNECTOR_PROTOCOL);

        connector.setScheme(CONNECTOR_SCHEME);
        connector.setSecure(false);
        connector.setPort(80);
        connector.setRedirectPort(443);

        return connector;
    }
}
