package com.example.app.service;

import com.example.app.model.Account;

public interface AccountService {
	
	public Account createAccount(Account entity);
	
	public Account getAccountbyIdNPin(Long accountNum, Integer pin);

}
