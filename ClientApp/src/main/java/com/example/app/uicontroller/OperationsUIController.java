package com.example.app.uicontroller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.RequestError;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OperationsUIController extends Runnable {

	public OperationsUIController setData(Object data);
	
	public OperationsUIController setOnSuccess(BiConsumer<String, Object> consumer);
	
	public OperationsUIController setOnError(Consumer<RequestError> consumer);
	
	public OperationsUIController setOnProgress(Consumer<Integer> consumer);
	
	public void makeDeposit() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void makeWithdrawal() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void deleteAccount() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void getAccountsBalance() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;
	
	public void externalOperation() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException;

}
