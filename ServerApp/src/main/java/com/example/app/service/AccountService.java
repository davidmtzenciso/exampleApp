package com.example.app.service;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsufficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;

public interface AccountService {
	
	public Account save(Account entity) throws FailedEntityValidationException;
	
	public Account getAccountByIdNPin(Long accountNum, Integer pin) throws AccountNotFoundException;
	
	public void deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException;
	
	public Double getBalance(Long id) throws AccountNotFoundException;
	
	public void addAmount(Double amount, long id) throws AccountNotFoundException;
	
	public void extractAmount(Double amount, long id) throws InsufficientFundsException, AccountNotFoundException;
	
	public static final String NOT_FOUND = "Account not found";
	public static final String OVERDRAWN = "Account's balance negative, unable to close";
	public static final String INSUFICIENT_FUNDS = "Operation unsuccessful, insuficient funds";
	public static final String LOGIN_FAILED = "Invalid account number or pin, please try again";
	public static final String FAILED_VALIDATION = "Unable to open account, Invalid data or missing required fields";

}
