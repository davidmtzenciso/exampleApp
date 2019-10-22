package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java8.En;


public class AuthenticationSteps implements En, DataInitialization {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Account newAccount;
	
	@Autowired
	private Credentials credentials;
	
	private ResultActions response;
	
	private final String HOST = "http://localhost:8080";
	private final String URI_MODULE = "/account";
	private final String URI_AUTH = "/auth";
	private final String API_VERSION = "/1.0.0";
	private final String SUCCEEDS = "SUCCEEDS";
	
public AuthenticationSteps() {
	
		Given("there is an account with id {long} and pin {int}", (Long id, Integer pin) -> {
			this.newAccount = this.initialize(this.newAccount);
			this.newAccount.setId(id);
			this.newAccount.setPin(pin);
			this.createAccount(this.newAccount);
		});
		
		Given("user provides the values {long} {int}", (Long id, Integer pin) -> {
			credentials.setAccountNumber(id);
			credentials.setPin(pin);
		});
		
		When("user {string}", (String testContext) -> {
			 this.response = mvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
				      .contentType(MediaType.APPLICATION_JSON)
				      .content((mapper.writeValueAsString(this.credentials))));
		});
		
		Then("authentication {string}", (String expectedResult) -> {	
			 response.andExpect(this.getExpectedStatus(expectedResult));			 
		});
	}
	
	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
	
	private void createAccount(Account account) throws Exception {
		this.response = mvc.perform(post(HOST + API_VERSION + URI_MODULE)
			      .contentType(MediaType.APPLICATION_JSON)
			      .content(mapper.writeValueAsString(account))
			      .accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());	 
	}
}
