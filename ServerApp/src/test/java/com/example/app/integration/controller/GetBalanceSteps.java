package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;

public class GetBalanceSteps implements En, DataInitialization  {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Account newAccount;
	
	private ResultActions response;
	private int id;
	
	private final String HOST = "http://localhost:8080";
	private final String API_VERSION = "/1.0.0";
	private final String URI_MODULE = "/account";
	private final String URI_GET_BALANCE = "/balance";
	private final String SUCCEEDS = "SUCCEEDS";
	
	public GetBalanceSteps() { 

		Given("user provides the id {int} of the account to query", (Integer id) -> {
			this.id = id;
		});
		
		When("user wants to get his account balance {string}", (String testContext) -> {
			 this.response = mvc.perform(get(HOST + API_VERSION + URI_MODULE + URI_GET_BALANCE + "?id=" + this.id)
				      .contentType(MediaType.APPLICATION_JSON_UTF8)
				      .accept(MediaType.APPLICATION_JSON_UTF8));	 
		});
		
		Then("the balance query {string}", (String expectedResult) -> {
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
			      .accept(MediaType.APPLICATION_JSON_UTF8));	
	}
	
	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
	
}

