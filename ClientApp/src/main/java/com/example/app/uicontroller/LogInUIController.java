package com.example.app.uicontroller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.RequestFailedException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.util.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogInUIController implements HttpClient {
	
	@Autowired
	private ObjectMapper mapper;
	
	private Account account;
	
	private final String WELCOME_MSG = "Bienvenido!";
	private final String LOGIN_ERROR = "Error al validar número de cuenta o PIN!\n favor de intentarlo de nuevo";
	private final String INTERNAL_ERROR = "Error del sistema, vuelvalo a intentar";
	private final String URI_AUTH = ("http://localhost:8080/auth");
	private final static Logger LOG = Logger.getLogger(LogInUIController.class.getName());

	public String authenticate(Credentials credentials)  {
		HttpEntity responseBody;
		
		try {
			responseBody = this.post(URI_AUTH, mapper.writeValueAsString(credentials));
			this.account = mapper.readValue(responseBody.getContent(), Account.class);
			return WELCOME_MSG;
		} 
		catch (JsonProcessingException e) {
			LOG.log(Level.SEVERE, "Error writing {0} to json, {1} ", new Object[] {Credentials.class, e});
		}
		catch (UnsupportedEncodingException e) {
			LOG.log(Level.SEVERE, "Error with instatiating StringEntity: {0}", e.getMessage());
		}
		catch (RequestFailedException e) {
			LOG.log(Level.WARNING, e.getMessage());
			return LOGIN_ERROR;
		}
		catch(InterruptedException | ExecutionException e) {
        	LOG.log(Level.SEVERE, "Error getting http response from future: {0}", e);	
		} 
		catch (IOException e) {
			LOG.log(Level.SEVERE, "Error closing http async client: {0}", e);
		} 
		
		return INTERNAL_ERROR;
	}
	
	public Account getAccount() {
		return this.account;
	}
}
