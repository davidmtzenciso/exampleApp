package com.example.app.conf;

import java.sql.Date;
import java.time.LocalDate;

import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface DataInitialization {

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
	
	default Transaction initialize(Transaction transaction, Account account) {
		transaction.setAccount(account);
		transaction.setAmount(100.0);
		transaction.setDate(Date.valueOf(LocalDate.now()));
		transaction.setDescription("oxxo inc.");
		transaction.setType(4);
		transaction.setId(null);
		return transaction;
	}
}
