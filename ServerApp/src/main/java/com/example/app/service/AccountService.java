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
	
	public Transaction makeDeposit(Transaction transaction) throws AccountNotFoundException, FailedEntityValidationException;
		
	public Transaction makeWithdrawal(Transaction transaction) throws InsuficientFundsException, AccountNotFoundException, FailedEntityValidationException;
	
	public String deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException;
	
	public Double getBalance(Long id) throws AccountNotFoundException;
	
	public static final String NOT_FOUND = "Account not found";
	public static final String OVERDRAWN = "Account's balance negative, unable to close";
	public static final String INSUFICIENT_FUNDS = "Operation unsuccessful, insuficient funds";
	public static final String LOGIN_FAILED = "Invalid account number or pin, please try again";
	public static final String FAILED_VALIDATION = "Unable to open account, Invalid data or missing required fields";
	public static final String EMPTY_LAST_NAME = "first name cannot be empty";
	public static final String EMPTY_FIRST_NAME = "last name cannot be empty";
	public static final String PIN_CERO = "account's pin number cannot be empty or be cero";
	public static final String INVALID_TRANSACTION = "Invalid data or missing required fields";
	public static final String ACCOUNT_NOT_PRESENT = "unable to process, no account data in the transaction";
	public static final String ID_DEFINED = "account number cannot be define, it is assigned";

	
}
