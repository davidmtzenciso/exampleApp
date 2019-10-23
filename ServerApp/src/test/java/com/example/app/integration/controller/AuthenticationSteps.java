package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.api.Scenario;
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

	@BeforeClass
	public void init() throws Exception {
		this.newAccount = this.initialize(this.newAccount);
		this.response = mvc.perform(post(HOST + API_VERSION + URI_MODULE)
			      .contentType(MediaType.APPLICATION_JSON_UTF8)
			      .content(mapper.writeValueAsString(newAccount))
			      .accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}


	@AfterClass
	public void clean(Scenario scenario) throws Exception {
		this.response = mvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=" + this.newAccount.getId())
			      .contentType(MediaType.APPLICATION_JSON_UTF8)
			      .accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());	 
	}
	
	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
}
