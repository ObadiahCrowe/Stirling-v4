package com.stirlinglms.stirling.config;

import com.stirlinglms.stirling.service.SpringUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpringUserService service;
    private final PasswordEncoder encoder;

    @Autowired
    SecurityConfig(SpringUserService service, PasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @Override
    public void configure(WebSecurity security) throws Exception {
        security.ignoring()
          .antMatchers("/error")
          .antMatchers(HttpMethod.POST, "/v1/user")
          .antMatchers(HttpMethod.POST, "/v1/email/verify");
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
          .csrf().disable()
          .authorizeRequests()
          .antMatchers("/v1/user").permitAll()
          .antMatchers("/oauth/token").permitAll()
          .anyRequest().authenticated()
          .and().anonymous().disable();
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        bean.setOrder(0);

        return bean;
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    public void getUserDetails(AuthenticationManagerBuilder builder) throws Exception {
        builder
          .userDetailsService(this.service)
          .passwordEncoder(this.encoder);
    }
}
