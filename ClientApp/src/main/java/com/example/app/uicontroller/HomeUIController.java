package com.example.app.uicontroller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.RequestError;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface HomeUIController {

	public HomeUIController setData(Object data);
	
	public HomeUIController setOnSuccess(BiConsumer<String, Account> consumer);
	
	public HomeUIController setOnError(Consumer<RequestError> consumer);
	
	public void openAccount() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
}
