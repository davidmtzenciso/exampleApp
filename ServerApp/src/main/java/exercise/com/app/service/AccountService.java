package exercise.com.app.service;

import exercise.com.app.model.Account;

public interface AccountService {
	
	public Account createAccount(Account entity);
	
	public Account getAccountbyIdNPin(Long accountNum, Integer pin);

}
