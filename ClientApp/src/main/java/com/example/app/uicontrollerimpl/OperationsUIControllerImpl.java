package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.RequestError;
import com.example.app.model.Transaction;
import com.example.app.uicontroller.OperationsUIController;
import com.example.app.util.AbstractUIController;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

public class OperationsUIControllerImpl extends AbstractUIController implements OperationsUIController  {
	
	@Autowired
	private URLBuilder builder;
		
	private List<Transaction> recent;
	
	private BiConsumer<String, Object> sendResponse;
	
	private final String TRANSACTION = ", transaction id: ";
	private final String DEPOSIT_OK = "\ndeposit successful!";
	private final String WITHDRAWAL_OK = "\nwithdrawal successful!";
	private final String GET_BALANCE_OK = "\ncurrent balance: ";
	private final String EXTERNAL_OK = "\noperation successful!";
	
	public void run() {
		this.recent = new ArrayList<>();
	}

	@Override
	public synchronized OperationsUIController setData(Object data) {
		this.data = data;
		return this;
	}

	@Override
	public synchronized OperationsUIController setOnSuccess(BiConsumer<String, Object> consumer) {
		this.sendResponse = consumer;
		return this;
	}

	@Override
	public synchronized OperationsUIController setOnError(Consumer<RequestError> consumer) {
		this.onError = consumer;
		return this;
	}
	
	public synchronized OperationsUIController setOnProgress(Consumer<Integer> consumer) {
		this.onProgress = consumer;
		return this;
	}

	public synchronized void makeDeposit()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onProgress.accept(0);
		this.onSuccess = data -> sendResponse.accept(DEPOSIT_OK + TRANSACTION + ((Transaction)data).getId(), recent.add((Transaction) data));
		this.post(builder.makeDeposit(), Transaction.class);
	}
	
	public synchronized void makeWithdrawal()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onProgress.accept(0);
		this.onSuccess = data -> sendResponse.accept(WITHDRAWAL_OK + TRANSACTION + ((Transaction)data).getId(), recent.add((Transaction) data));
		this.post(builder.makeWithdrawal(), Transaction.class);
	}

	public synchronized void deleteAccount()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onProgress.accept(0);
		this.onSuccess = data -> sendResponse.accept("\n" + String.valueOf(data) , null);
		this.delete(builder.deleteAccount((Long)data), String.class);
	}
	
	public synchronized void getAccountsBalance()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onProgress.accept(0);
		this.onSuccess = data -> sendResponse.accept(GET_BALANCE_OK + String.valueOf(data) , null);
		this.get(builder.getBalance((Long)data), Double.class);
	}
	
	public synchronized void externalOperation() throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.onProgress.accept(0);
		this.onSuccess = data -> sendResponse.accept(EXTERNAL_OK + TRANSACTION + String.valueOf(data) , null);
		this.post(builder.externalDebitsNChecks(), Long.class);
	}

}
