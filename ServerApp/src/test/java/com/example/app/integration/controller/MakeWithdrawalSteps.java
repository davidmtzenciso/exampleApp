package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

public class MakeWithdrawalSteps implements En, DataInitialization {

	@Autowired
    private MockMvc mvc;
		
	@Autowired
	private ObjectMapper mapper;
	
	private ResultActions response;
	
	private Transaction transaction;
	
	private final String HOST = "http://localhost:8080";
	private final String API_VERSION = "/1.0.0";
	private final String URI_MODULE = "/account";
	private final String URI_MAKE_WITHDRAWAL = "/withdrawal";
	private final String SUCCEEDS = "SUCCEEDS";
	
	public MakeWithdrawalSteps() { 

		Given("user provides the values to withdrawal funds", (DataTable transactionDt) -> {
			List<Transaction> table = transactionDt.asList(Transaction.class);
			this.transaction = table.get(0);
		});
		
		When("user wants to make a withdrawal {string}", (String testContext) -> {
			 this.response = mvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_WITHDRAWAL)
				      .contentType(MediaType.APPLICATION_JSON_UTF8)
				      .content(mapper.writeValueAsString(this.transaction))
				      .accept(MediaType.APPLICATION_JSON_UTF8));	 
		});
		
		Then("the withdrawal {string}", (String expectedResult) -> {
			 response.andExpect(this.getExpectedStatus(expectedResult));
		});
	}
	
	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
}
