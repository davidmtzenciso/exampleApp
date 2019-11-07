package com.example.app.service;

import java.util.List;

import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.TransactionsNotFoundException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface TransactionService {
		
	public Transaction save(Transaction transaction) throws FailedEntityValidationException;
	
	public List<Transaction> getlastFive(Account account) throws TransactionsNotFoundException;
	
	public static final String INVALID_TRANSACTION = "Invalid data or missing required fields";
	public static final String NOT_FOUND = "unable to process, tranasctions where not found for the given account";

}
