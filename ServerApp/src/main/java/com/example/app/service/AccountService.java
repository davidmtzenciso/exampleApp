package com.example.app.service;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface AccountService {
	
	public Account save(Account entity) throws FailedEntityValidationException;
	
	public Account getAccountbyIdNPin(Long accountNum, Integer pin) throws AccountNotFoundException;
	
	public Transaction makeDeposit(Transaction transaction) throws AccountNotFoundException;
		
	public Transaction makeWithdrawal(Transaction transaction) throws InsuficientFundsException, AccountNotFoundException;
	
	public String deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException;
	
	public Double getBalance(Long id) throws AccountNotFoundException;
	
	public final static int DEPOSIT = 1;
	public final static int WITHDRAWAL = 2;
	public final static int DEBITS_CHECKS = 3;
	
}
