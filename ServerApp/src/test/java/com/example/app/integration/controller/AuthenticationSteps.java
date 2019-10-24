package com.example.app.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java8.En;

public class AuthenticationSteps implements En {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;

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
			 MvcResult result = response.andExpect(this.getExpectedStatus(expectedResult))
			 	     .andReturn();
			 if(expectedResult.equals(SUCCEEDS)) {
				 Assert.assertEquals(5, mapper.readValue(result.getResponse().getContentAsByteArray(), Account.class).getTransactions().size());
			 }
		});
	}

	private ResultMatcher getExpectedStatus(String expectedResult) {
		return expectedResult.equals(SUCCEEDS) ? status().isOk() : status().isUnprocessableEntity();
	}
}
