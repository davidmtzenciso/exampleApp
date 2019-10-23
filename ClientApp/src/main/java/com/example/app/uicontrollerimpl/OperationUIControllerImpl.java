package com.example.app.uicontrollerimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Transaction;
import com.example.app.uicontroller.OperationsUIController;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

public class OperationUIControllerImpl extends AbstractUIController implements OperationsUIController  {
	
	@Autowired
	private URLBuilder builder;
	
	private List<Transaction> recentTransactions;
	
	public OperationUIControllerImpl() {
		this.recentTransactions = new ArrayList<>();
	}

	@Override
	public OperationsUIController setData(Object data) {
		this.data = data;
		return this;
	}

	@Override
	public OperationsUIController setOnSuccess(Consumer<String> consumer) {
		this.onSuccess = consumer;
		return this;
	}

	@Override
	public OperationsUIController setOnError(Consumer<String> consumer) {
		this.onError = consumer;
		return this;
	}

	public void makeDeposit()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.post(builder.makeDeposit(), Transaction.class, data -> this.recentTransactions.add((Transaction)data));
	}
	
	public void makeWithdrawal()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.post(builder.makeWithdrawal(), Transaction.class, data -> this.recentTransactions.add((Transaction)data));
	}

	public void deleteAccount()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.delete(builder.deleteAccount((Long)data), Transaction.class);
	}
	
	public void getBalance()  throws MalformedRequestException, UnsupportedEncodingException, JsonProcessingException, IOException {
		this.delete(builder.getBalance((Long)data), Double.class);
	}
	
}
