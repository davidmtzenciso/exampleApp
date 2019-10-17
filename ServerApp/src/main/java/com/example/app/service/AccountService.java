package com.example.app.service;

import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface AccountService {
	
	public Account save(Account entity);
	
	public Account getAccountbyIdNPin(Long accountNum, Integer pin);
		
	public Account save(Transaction transaction) throws InsuficientFundsException;
	
	public String deleteAccount(Long id) throws OverdrawnAccountException;
	
	public double getBalance(Long id);
	
	public final static int DEPOSIT = 1;
	public final static int WITHDRAWAL = 2;
	public final static int DEBITS_CHECKS = 3;
	
}
