package com.example.app.service;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface AccountService {
	
	public Account save(Account entity) throws FailedEntityValidationException;
	
	public Account getAccountByIdNPin(Long accountNum, Integer pin) throws AccountNotFoundException;
	
	public Transaction makeDeposit(Transaction transaction) throws AccountNotFoundException, FailedEntityValidationException;
		
	public Transaction makeWithdrawal(Transaction transaction) throws InsuficientFundsException, AccountNotFoundException, FailedEntityValidationException;
	
	public String deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException;
	
	public Double getBalance(Long id) throws AccountNotFoundException;
	
	public static final String NOT_FOUND = "\nAccount not found";
	public static final String OVERDRAWN = "\nAccount's balance negative, unable to close";
	public static final String INSUFICIENT_FUNDS = "\nOperation unsuccessful, insuficient funds";
	public static final String LOGIN_FAILED = "\nInvalid account number or pin, please try again";
	public static final String FAILED_VALIDATION = "\nUnable to open account, Invalid data or missing required fields";
	public static final String EMPTY_LAST_NAME = "\nfirst name cannot be empty";
	public static final String EMPTY_FIRST_NAME = "\nlast name cannot be empty";
	public static final String PIN_CERO = "\naccount's pin number cannot be empty or be cero";
	public static final String INVALID_TRANSACTION = "\nInvalid data or missing required fields";
	public static final String ACCOUNT_NOT_PRESENT = "\nunable to process, no account data in the transaction";
	public static final String ID_DEFINED = "\naccount number cannot be define, it is assigned";
	public static final String CLOSE_ACCOUNT_OK = "\nAccount closed!";

	
}
