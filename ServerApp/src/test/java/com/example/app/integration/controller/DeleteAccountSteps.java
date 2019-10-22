package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java8.En;


public class DeleteAccountSteps implements En, DataInitialization  {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Account newAccount;
	
	private ResultActions response;
	private Integer id;
	
	private final String HOST = "http://localhost:8080";
	private final String URI_MODULE = "/account";
	private final String API_VERSION = "/1.0.0";
	private final String FAILS = "FAILS";
	private final String SUCCEEDS = "SUCCEEDS";
	
	public DeleteAccountSteps() { 
		
		Given("there is an account with id {int}", (Integer id) -> {
			this.newAccount = this.initialize(this.newAccount);
			this.newAccount.setId(new Long(1));
			this.createAccount(this.newAccount);
		});

		Given("user wants to delete his account by providing {int}", (Integer accountNumber) -> {
			this.id = accountNumber;
		});
		
		When("user wants to delete the account {string}", (String testContext) -> {
			 this.response = mvc.perform(post(HOST + API_VERSION + URI_MODULE + "?id=" + this.id)
				      .contentType(MediaType.APPLICATION_JSON)
				      .accept(MediaType.APPLICATION_JSON_UTF8));	 
		});
		
		Then("delete {string}", (String expectedResult) -> {
			 response.andExpect(this.getExpectedStatus(expectedResult))
			 		 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
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
