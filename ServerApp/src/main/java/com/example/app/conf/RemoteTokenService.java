package com.example.app.conf;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoteTokenService implements ResourceServerTokenServices {

	private RestTemplate restTemplate;
	
	private AccessTokenConverter tokenConverter;
	
	private ObjectMapper mapper;
	
	private static final String CHECK_TOKEN_PATH = "http://localhost:8095/oauth/check_token?token=";
	private static final String TOKEN_ERROR = "Token not allowed";
	
	public RemoteTokenService() {
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			public void handleError(ClientHttpResponse response) throws IOException {
				if(response.getRawStatusCode() != 400) {
					super.handleError(response);
				}
			}
		});
	}
	
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.exchange(CHECK_TOKEN_PATH, HttpMethod.GET, new HttpEntity<MultiValueMap<String, String>>(null, headers), Map.class).getBody();
		if(map != null) {
			return tokenConverter.extractAuthentication(map);
		} else {
			throw new InvalidTokenException(TOKEN_ERROR);
		}
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		try {
			return mapper.readValue(accessToken, new TypeReference<OAuth2AccessToken>() {});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
