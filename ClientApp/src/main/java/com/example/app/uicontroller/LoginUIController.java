package com.example.app.uicontroller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import com.example.app.exceptions.AuthenticationFailedException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoginUIController  {
	
	public LoginUIController setData(Object data);
	
	public LoginUIController setOnSuccess(Consumer<String> consumer);
	
	public LoginUIController setOnError(Consumer<String> consumer);
	
	public void authenticate() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public Account getActiveAccount() throws AuthenticationFailedException;
	
}
