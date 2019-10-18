package com.example.app.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.app.conf.DataInitialization;
import com.example.app.controller.AccountController;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = { "com.example.app.conf", "com.example.app.controller", "com.example.app.service"}) 
@WebMvcTest(AccountController.class)
public class DebitsCheckControllerTest implements DataInitialization {

	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockBean
	 private AccountService accountService;
	 
	 @Autowired
	 private ObjectMapper mapper;
	 
	 @Autowired
	 private Account account;
	 
	 @Autowired
	 private Transaction transaction;
	 
	 @Autowired
	 private Credentials credentials;
	 
	 private final String HOST = "http://localhost:8080";
	 private final String URI_MODULE = "/account";
	 private final String API_VERSION = "/1.0.0";
	 private final String URI_AUTH = "/auth";
	 private final String URI_MAKE_DEPOSIT = "/deposit";
	 
	 @PostConstruct
	 public void init() {
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
	 }
	 
	 @Before
	 public void initData() {
		this.account = this.initialize(this.account);
		this.transaction = this.initialize(transaction, this.account);
		this.credentials = this.initialize(credentials);
	 }
	 
	 //			SAVE ACCOUNT TESTS

	 @Test
	 public void testCorrectSaveAccount() throws JsonProcessingException, Exception  {
		 when(accountService.save(this.account)).thenReturn(this.account);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.account)))
		 				.andExpect(status().isOk());
	 }
	 
	 @Test
	 public void testSaveAccountWithIncorrectBody() throws JsonProcessingException, Exception  {
		 when(accountService.save(this.account)).thenThrow(FailedEntityValidationException.class);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.account)))
		 				.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testSaveAccountWithIncorretnContentType() throws JsonProcessingException, Exception  {
		 when(accountService.save(this.account)).thenThrow(FailedEntityValidationException.class);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_XML)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.account)))
		 				.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 
	 //			DELETE ACCOUNT TESTS
	 
	 @Test
	 public void testDeleteWithoutParam() throws JsonProcessingException, Exception {
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE)
				 		.contentType(MediaType.APPLICATION_JSON_UTF8)
				 		.accept(MediaType.APPLICATION_JSON_UTF8)
				 		.content(mapper.writeValueAsString(new Long(1))))
		 				.andExpect(status().is4xxClientError());
	 }
	 
	 @Test
	 public void testDeleteExistingAccountWithParam() throws JsonProcessingException, Exception {
		 when(accountService.save(this.account)).thenReturn(this.account);
		 
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=1")
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
	 				.andExpect(status().isOk());
	 }
	 
	 @Test
	 public void testDeleteNonExistingAccountWithParam() throws Exception {
		 when(accountService.deleteAccount(new Long(0))).thenThrow(AccountNotFoundException.class);
		 
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=0")
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
	 				.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testDeleteOverdrawnAccountWithParam() throws Exception {
		 when(accountService.deleteAccount(new Long(1))).thenThrow(OverdrawnAccountException.class);
		 
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=1")
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
	 				.andExpect(status().isUnprocessableEntity());
	 }
	 
	 //			GET ACCOUNT BALANCE TESTS
	 	 
	 
	 
	 //			AUTHENTICATION TESTS
	 
	 @Test
	 public void testCorrectAuthentication() throws JsonProcessingException, Exception {
		 when(accountService.getAccountbyIdNPin(credentials.getAccountNumber(), credentials.getPin())).thenReturn(this.account);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.credentials)))
			 		.andExpect(status().isOk());
		 
	 }
	 
	 @Test
	 public void testAuthenticationeWrongBody() throws JsonProcessingException, Exception {		
		 this.transaction.setAccount(null);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
				 		.contentType(MediaType.APPLICATION_JSON_UTF8)
				 		.accept(MediaType.APPLICATION_JSON_UTF8)
				 		.content(mapper.writeValueAsString(this.transaction)))
				 		.andExpect(status().isOk());
	 }
	 
	 @Test
	 public void testFailedAuthentication() throws JsonProcessingException, Exception {
		 when(accountService.getAccountbyIdNPin(this.credentials.getAccountNumber(), this.credentials.getPin())).
		 thenThrow(AccountNotFoundException.class);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.credentials)))
			 		.andExpect(status().isUnprocessableEntity());
	 }
	 
	 //			MAKE DEPOSIT TESTS
	 
	 @Test
	 public void testCorrectDeposit() throws JsonProcessingException, Exception {
		 when(accountService.makeDeposit(transaction)).thenReturn(this.transaction);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_DEPOSIT)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
			 		.andExpect(status().isOk());
	 }
	 
	 //			MAKE WITHDRAWAL TESTS
	 
	 
	 
}
