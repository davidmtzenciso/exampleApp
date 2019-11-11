package com.example.app.conf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	private static final String RESOURCE_ID = "resource-server-rest-api";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(RESOURCE_ID).authenticationManager(authenticationManagerBean())
				  .tokenExtractor( new TokenExtractor() {
						@Override
						public Authentication extract(HttpServletRequest request) {
							return null;
						}});
	}

	@Bean
	public ResourceServerTokenServices tokenService() {
		return new RemoteTokenService();
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
		authenticationManager.setTokenServices(tokenService());
		return authenticationManager;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable().anonymous().and().authorizeRequests().antMatchers("/account/**").authenticated()
				.antMatchers("/login/**").permitAll();
	}
}
