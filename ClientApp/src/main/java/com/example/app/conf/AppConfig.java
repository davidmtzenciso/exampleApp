package com.example.app.conf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.ui.HomeUI;
import com.example.app.ui.LoginUI;
import com.example.app.uicontroller.LoginUIController;
import com.example.app.uicontrollerimpl.LoginUIControllerImpl;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
	
	@Bean
	public LoginUI logInUI() {
		return new LoginUI();
	}
	
	@Bean
	public HomeUI homeUI() {
		return new HomeUI();
	}
	
	@Bean
	public LoginUIController logInUIController() {
		return new LoginUIControllerImpl();
	}
	
	@Bean
	public URLBuilder urlBuilder() {
		return new URLBuilder();
	}
	
	@Bean
	@Scope("prototype")
	public BufferedReader bufferedReader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
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
