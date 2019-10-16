package com.example.app.conf;

import com.example.app.model.Account;

public interface InitAccount {

	default Account initialize(Account account) {
		account.setId(null);
		account.setPin(1234);
		account.setFirstName("david");
		account.setLastName("Martinez Enciso");
		account.setBalance(1000.0);
		account.setAccountHoldersId("12345678");
		account.setTransactions(null);
		return account;
	}
}
