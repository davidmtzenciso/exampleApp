package com.exercise.app.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.beans.factory.annotation.Autowired;

import com.exercise.app.exceptions.LoginFailedException;
import com.exercise.app.model.Credentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogInUIController {
	
	@Autowired
	private ObjectMapper mapper;
	
	private final String WELCOME_MSG = "Bienvenido!";
	private final String LOGIN_ERROR = "Error al validar número de cuenta o PIN!\n favor de intentarlo de nuevo";
	private final String INTERNAL_ERROR = "Error del sistema, vuelvalo a intentar";
	private final String URI_AUTH = ("http://localhost:8080/auth");
	private final static Logger LOG = Logger.getLogger(LogInUIController.class.getName());

	public String authenticate(Credentials credentials) throws LoginFailedException {
		HttpResponse response;
		CloseableHttpAsyncClient httpclient;
		HttpPost post;
		
        
        httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            post = new HttpPost(URI_AUTH);
            post.setEntity(new StringEntity(mapper.writeValueAsString(credentials)));
            Future<HttpResponse> future = httpclient.execute(post, null);
            response =  future.get();
            if(response.getStatusLine().getStatusCode() == 200) {
            	return WELCOME_MSG;
            } else {
            	throw new LoginFailedException(LOGIN_ERROR);
            }
        } catch(InterruptedException | ExecutionException e) {
        	LOG.log(Level.SEVERE, "Error getting http response from future: {0}", e.getMessage());
        	throw new LoginFailedException(INTERNAL_ERROR);
		} catch (JsonProcessingException e) {
			LOG.log(Level.SEVERE, "Error converting credentials to json: {0} ", e.getMessage());
			throw new LoginFailedException(INTERNAL_ERROR);
		} catch (UnsupportedEncodingException e) {
			LOG.log(Level.SEVERE, "Error with instatiating StringEntity: {0}", e.getMessage());
			throw new LoginFailedException(INTERNAL_ERROR);
		}
        finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				LOG.log(Level.WARNING, "Error closing Http Client, {0}", e.getMessage());
			}
        }
	}
}
