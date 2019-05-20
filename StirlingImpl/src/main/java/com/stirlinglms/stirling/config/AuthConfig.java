package com.stirlinglms.stirling.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    private static final String CLIENT_ID = "stirling";
    private static final String CLIENT_SECRET = "gstudent-dundee-developer-beta-031";

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_AUTHORISATION_CODE = "authorisation_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String GRANT_TYPE_IMPLICIT = "implicit";

    private static final String SCOPE_READ = "read";
    private static final String SCOPE_WRITE = "write";
    private static final String SCOPE_TRUST  = "trust";

    private static final int ACCESS_TOKEN_VALIDITY = 60 * 60;
    private static final int REFRESH_TOKEN_VALIDITY = 6 * 60 * 60;

    private final TokenStore store;
    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;

    @Autowired
    AuthConfig(@Qualifier("tokenStore") TokenStore store, AuthenticationManager manager, PasswordEncoder encoder) {
        this.store = store;
        this.manager = manager;
        this.encoder = encoder;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer
          .inMemory()
          .withClient(CLIENT_ID)
          .secret(this.encoder.encode(CLIENT_SECRET))
          .authorizedGrantTypes(GRANT_TYPE_PASSWORD, GRANT_TYPE_AUTHORISATION_CODE, GRANT_TYPE_REFRESH_TOKEN, GRANT_TYPE_IMPLICIT)
          .scopes(SCOPE_READ, SCOPE_WRITE, SCOPE_TRUST)
          .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY)
          .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer configurer) throws Exception {
        configurer
          .tokenStore(this.store)
          .authenticationManager(this.manager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer configurer) throws Exception {
        configurer
          .tokenKeyAccess("permitAll()")
          .checkTokenAccess("isAuthenticated()");
    }
}
