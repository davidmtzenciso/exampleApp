package com.example.app.conf;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;

@Configuration
public class ModelAppConfig {
	
	@Bean
	@Scope("prototype")
	public Credentials credentials() {
		return new Credentials();
	}
	
	@Bean
	@Scope("prototype")
	public Account account() {
		Account account = new Account();
		account.setTransactions(new ArrayList<>());
		return account;
	}
	
	@Bean
	@Scope("prototype")
	public Transaction transaction() {
		return new Transaction();
	}
	

}
