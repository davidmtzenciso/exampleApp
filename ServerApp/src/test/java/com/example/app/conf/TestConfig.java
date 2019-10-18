package com.example.app.conf;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;
import com.example.app.serviceImpl.AccountServiceImpl;

@TestConfiguration
public class TestConfig {

	@Bean 
	public AccountService accountService( ) {
		return new AccountServiceImpl();
	}
	
	@Bean
	public Account account() {
		return new Account();
	}
	
	@Bean
	public Transaction transaction() {
		return new Transaction();
	}
	
	@Bean
	public Credentials credentials() {
		return new Credentials();
	}
}

