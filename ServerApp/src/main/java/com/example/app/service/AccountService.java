package com.example.app.service;

import com.example.app.exception.InsuficientFundsException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface AccountService {
	
	public Account createAccount(Account entity);
	
	public Account getAccountbyIdNPin(Long accountNum, Integer pin);
	
	public Account findAndLockById(Long id);
	
	public Account saveTransaction(Transaction transaction) throws InsuficientFundsException;
}
