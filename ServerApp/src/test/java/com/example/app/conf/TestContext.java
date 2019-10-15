package com.example.app.conf;

import java.util.LinkedHashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.app.model.Account;
import com.example.app.model.Transaction;

@Configuration
public class TestContext {

	@Bean
	public Account account () {
		Account account = new Account();
		account.setTransactions(new LinkedHashSet<>());
		return account;
	}
	
	@Bean
	public Transaction transaction() {
		return new Transaction();
	}
}
