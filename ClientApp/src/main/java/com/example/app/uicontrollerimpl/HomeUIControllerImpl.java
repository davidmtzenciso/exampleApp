package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.RequestError;
import com.example.app.uicontroller.HomeUIController;
import com.example.app.util.AbstractUIController;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

public class HomeUIControllerImpl extends AbstractUIController implements HomeUIController {
	
	@Autowired
	private URLBuilder builder;
		
	private BiConsumer<String, Account> sendResponse;
	
	@Override
	public HomeUIController setData(Object data) {
		this.data = data;
		return this;
	}

	@Override
	public HomeUIController setOnSuccess(BiConsumer<String, Account> consumer) {
		this.sendResponse = consumer;
		return this;
	}

	@Override
	public HomeUIController setOnError(Consumer<RequestError> consumer) {
		this.onError = consumer;
		return this;
	}
	
	@Override
	public void openAccount() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onSuccess = data -> this.sendResponse.accept("Account opened!", (Account)data);
		this.post(builder.createAccount(), Account.class);
	}

}
