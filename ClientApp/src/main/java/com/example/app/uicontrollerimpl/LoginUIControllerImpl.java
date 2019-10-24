package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.AuthenticationFailedException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.uicontroller.LoginUIController;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LoginUIControllerImpl extends AbstractUIController implements LoginUIController{
	
	@Autowired
	private URLBuilder builder;
	
	private Account activeAccount;
		
	private final String AUTH_FAILED = "Authentication not successful";
	
	@Override
	public LoginUIController setData(Object data) {
		this.data = data;
		return this;
	}
	
	@Override
	public LoginUIController setOnSuccess(Consumer<String> consumer) {
		this.onSuccess = consumer;
		return this;
	}
	
	@Override
	public LoginUIController setOnError(Consumer<String> consumer) {
		this.onError = consumer;
		return this;
	}

	@Override
	public void authenticate() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.post(builder.authentication(), Account.class, data -> this.activeAccount = (Account) data);
	}
	
	@Override
	public Account getActiveAccount() throws AuthenticationFailedException {
		if(this.activeAccount == null) {
			throw new AuthenticationFailedException(AUTH_FAILED);
		} else {
			return this.activeAccount;
		}
	}
	
}
