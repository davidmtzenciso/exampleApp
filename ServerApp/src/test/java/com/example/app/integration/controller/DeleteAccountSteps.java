package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.BeforeClass;
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
	private int id;
	
	private final String HOST = "http://localhost:8080";
	private final String URI_MODULE = "/account";
	private final String API_VERSION = "/1.0.0";
	private final String SUCCEEDS = "SUCCEEDS";
	
	public DeleteAccountSteps() { 
		
		Given("user provides the id {int} of the account to delete", (Integer id) -> {
			this.id = id;
		});
		
		When("user wants to delete the account {string}", (String testContext) -> {
			 this.response = mvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=" + this.id)
				      .contentType(MediaType.APPLICATION_JSON_UTF8)
				      .accept(MediaType.APPLICATION_JSON_UTF8));	 
		});
		
		Then("delete {string}", (String expectedResult) -> {
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
	
	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
}
