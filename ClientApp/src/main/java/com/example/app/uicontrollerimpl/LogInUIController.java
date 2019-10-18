package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.uicontroller.UIController;
import com.example.app.util.HttpCommunication;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogInUIController implements UIController, HttpCommunication {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private URLBuilder builder;
		
	private final String WELCOME_MSG = "Bienvenido!";
	private final String LOGIN_ERROR = "Error al validar número de cuenta o PIN!\n favor de intentarlo de nuevo";
	private Object data;
	private Consumer<String> onSuccess;
	private Consumer<String> onError;
	private CloseableHttpAsyncClient client;
	
	@Override
	public UIController setData(Object data) {
		this.data = data;
		return this;
	}
	
	@Override
	public UIController setOnSuccess(Consumer<String> consumer) {
		this.onSuccess = consumer;
		return this;
	}
	
	@Override
	public UIController setOnError(Consumer<String> consumer) {
		this.onError = consumer;
		return this;
	}

	public void authenticate() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		if(this.isMalformed(data, onSuccess, onError)) {
			throw new MalformedRequestException("set data and callbacks to handle request");
		} else {
			this.post(builder.postAuth(), 
					mapper.writeValueAsString(data), 
					response -> {
						int status = response.getStatusLine().getStatusCode();
						if(status == 200) {
							onSuccess.accept(WELCOME_MSG);
						} else {
							onError.accept(LOGIN_ERROR);
						}
					}, 
					error -> {
						onError.accept(SERVER_ERROR);
					}).close();
		}
	}
	
}
