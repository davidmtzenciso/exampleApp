package com.example.app.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.model.dto.CustomUserDetails;

@Configuration
public class TestAppConfig {

	@Bean
	@Scope("prototype")
	public Account account() {
		return new Account();
	}
	
	@Bean
	@Scope("prototype")
	public Transaction transaction() {
		return new Transaction();
	}
	
	@Bean
	@Scope("prototype")
	public Credentials credentials() {
		return new Credentials();
	}
	
	@Bean
	@Scope("prototype")
	public CustomUserDetails customUserDetails() {
		return new CustomUserDetails();
	}
	
}
