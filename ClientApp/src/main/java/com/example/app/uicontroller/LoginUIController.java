package com.example.app.uicontroller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.RequestError;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoginUIController  {
	
	public LoginUIController setData(Object data);
	
	public LoginUIController setOnSuccess(BiConsumer<String, Account> consumer);
	
	public LoginUIController setOnError(Consumer<RequestError> consumer);
	
	public void authenticate() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
		
}
