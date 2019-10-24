package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.RequestError;
import com.example.app.uicontroller.LoginUIController;
import com.example.app.util.AbstractUIController;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LoginUIControllerImpl extends AbstractUIController implements LoginUIController{
	
	@Autowired
	private URLBuilder builder;
		
	private BiConsumer<String, Account> sendResponse;
			
	@Override
	public synchronized LoginUIController setData(Object data) {
		this.data = data;
		return this;
	}
	
	@Override
	public synchronized LoginUIController setOnSuccess(BiConsumer<String, Account> consumer) {
		this.sendResponse = consumer;
		return this;
	}
	
	@Override
	public synchronized LoginUIController setOnError(Consumer<RequestError> consumer) {
		this.onError = consumer;
		return this;
	}

	@Override
	public synchronized void authenticate() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onSuccess = data -> sendResponse.accept("Authenticated! Welcome", (Account) data);
		this.post(builder.authentication(), Account.class);
	}
	
}
