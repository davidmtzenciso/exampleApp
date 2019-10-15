package com.example.app.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.app.conf.InitAccount;
import com.example.app.controller.DebitsCheckController;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = { "com.example.app.conf", "com.example.app.controller", "com.example.app.service"}) 
@WebMvcTest(DebitsCheckController.class)
public class DebitsCheckControllerTest implements InitAccount {

	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockBean
	 private AccountService accountService;
	 
	 @Autowired
	 private ObjectMapper mapper;
	 
	 @Autowired
	 private Account newAccount;
	 
	 @Autowired
	 private Transaction transaction;
	 
	 private final String URI_MODULE = "/debits-check";
	 private final String API_VERSION = "/1.0.0";
	 private final String URI_POST_ACCOUNT = "/account";
	 private final String URI_POST_ACCOUNT_AUTH = "/account/auth";
	 
	 @PostConstruct
	 public void init() {
		this.newAccount.setTransactions(new LinkedHashSet<>());
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
	 }
	 
	 @Before
	 public void initData() {
		this.newAccount = this.initialize(this.newAccount);
	 }

	 @Test
	 public void testCorrectPost() throws JsonProcessingException, Exception  {
		 when(accountService.createAccount(this.newAccount)).thenReturn(this.newAccount);
		 
		 mockMvc.perform(post(API_VERSION + URI_MODULE + URI_POST_ACCOUNT)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.newAccount)))
		 				.andExpect(status().isOk());
	 }
	 
	 @Test
	 public void testGetAccountByIdNPinWrongRequestBody() throws JsonProcessingException, Exception {
		 when(accountService.getAccountbyIdNPin(new Long(1), 1234)).thenReturn(this.newAccount);
		 
		 mockMvc.perform(post(API_VERSION + URI_MODULE + URI_POST_ACCOUNT_AUTH)
				 		.contentType(MediaType.APPLICATION_JSON_UTF8)
				 		.accept(MediaType.APPLICATION_JSON_UTF8)
				 		.content(mapper.writeValueAsString(this.transaction)))
				 		.andExpect(status().isOk());
	 }
}
