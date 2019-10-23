package com.example.app.uicontroller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import com.example.app.exceptions.MalformedRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OperationsUIController {

	public OperationsUIController setData(Object data);
	
	public OperationsUIController setOnSuccess(Consumer<String> consumer);
	
	public OperationsUIController setOnError(Consumer<String> consumer);
	
	public void makeDeposit() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void makeWithdrawal() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void deleteAccount() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void getBalance() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;

}
