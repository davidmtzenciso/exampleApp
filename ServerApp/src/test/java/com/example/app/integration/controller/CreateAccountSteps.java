package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.app.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

public class CreateAccountSteps implements En {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Account newAccount;
	
	private ResultActions response;
	
	private final String HOST = "http://localhost:8080";
	private final String URI_MODULE = "/account";
	private final String API_VERSION = "/1.0.0";
	private final String SUCCEEDS = "SUCCEEDS";
	
	public CreateAccountSteps() {
		
		Given("user wants to create an account with the values", (DataTable accountDt) -> {
			List<Account> accounts = accountDt.asList(Account.class);
			this.newAccount = accounts.get(0);
		});
		
		When("user wants to save the new account {string}", (String testContext) -> {
			 this.response = mvc.perform(post(HOST + API_VERSION + URI_MODULE)
				      .contentType(MediaType.APPLICATION_JSON_UTF8)
				      .content((mapper.writeValueAsString(this.newAccount))));
		});
		
		Then("save {string}", (String expectedResult) -> {	
			 response.andExpect(this.getExpectedStatus(expectedResult));			 
		});
	}

	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
}
