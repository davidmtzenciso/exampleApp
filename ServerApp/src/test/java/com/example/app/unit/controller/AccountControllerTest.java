package com.example.app.unit.controller;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.example.app.conf.DataInitialization;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest implements DataInitialization {
	
	@Autowired
	private MockMvc mockMvc;
	 
	@MockBean
	private AccountService accountServiceMock;
	
	@MockBean
	private AccountService transactionServiceMock;
	 
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
	private final String URI_BALANCE = "/balance";
	private final String URI_MAKE_WITHDRAWAL = "/withdrawal";
	private final String URI_DEBITS_CHECKS = "/external/debits-checks";
	private final String WRONG_BODY = "{'name':'jorge', middleName:'daniel'}";
	 
	 @PostConstruct
	 public void init() {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	 }
	 
	 @Before
	 public void initData() {
		this.account = this.initialize(this.account);
		this.transaction = this.initialize(transaction, this.account);
		this.credentials = this.initialize(credentials);
	 }
	 
	 //			SAVE ACCOUNT TESTS
	 
	 @Test
	 public void testSaveAccountWithIncorretEntryContentType() throws JsonProcessingException, Exception  {
		 when(accountServiceMock.save(Mockito.any(Account.class))).thenThrow(FailedEntityValidationException.class);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_XML)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.account)))
		 				.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testSaveAccountWithIncorrectBody() throws JsonProcessingException, Exception  {

		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(WRONG_BODY))
		 				.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testSaveAccountWithInvalidData() throws JsonProcessingException, Exception  {
		 when(accountServiceMock.save(Mockito.any(Account.class))).thenThrow(FailedEntityValidationException.class);

		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.account)))
		 				.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testCorrectSaveAccount() throws JsonProcessingException, Exception  {
		 when(accountServiceMock.save(Mockito.any(Account.class))).thenReturn(this.account);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.account)))
		 				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		 				.andExpect(status().isOk());	 
	 }
	 
	 
	 //			DELETE ACCOUNT TESTS
	 
	 @Test
	 public void testDeleteWithIncorrectContentType() throws JsonProcessingException, Exception {
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE)
				 		.contentType(MediaType.APPLICATION_XML)
				 		.accept(MediaType.APPLICATION_JSON_UTF8)
				 		.content(mapper.writeValueAsString(new Long(1))))
		 				.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testDeleteWithoutParam() throws JsonProcessingException, Exception {
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE)
				 		.contentType(MediaType.APPLICATION_JSON_UTF8)
				 		.accept(MediaType.APPLICATION_JSON_UTF8)
				 		.content(mapper.writeValueAsString(new Long(1))))
		 				.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testDeleteNonExistingAccountWithParam() throws Exception {
		 
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=1")
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
	 				.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testDeleteOverdrawnAccountWithParam() throws Exception {
		 
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=1")
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
	 				.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testDeleteExistingAccountWithParam() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(delete(HOST + API_VERSION + URI_MODULE + "?id=1")
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
		 			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	 				.andExpect(status().isOk());
	 }
	 
	 //			GET ACCOUNT BALANCE TESTS
	 
	 @Test
	 public void testGetBalanceWitIncorrectContentType() throws Exception {
		 
		 mockMvc.perform(get(HOST + API_VERSION + URI_MODULE + URI_BALANCE + "?id=1")
				 .contentType(MediaType.APPLICATION_XML)
				 .accept(MediaType.APPLICATION_JSON_UTF8))
		 		 .andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testGetBalanceWithoutParam() throws Exception {
		 
		 mockMvc.perform(get(HOST + API_VERSION + URI_MODULE + URI_BALANCE)
				 .contentType(MediaType.APPLICATION_JSON_UTF8)
				 .accept(MediaType.APPLICATION_JSON_UTF8))
		 		 .andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testGetBalanceOfNonExistingAccountWithParam() throws Exception {
		 when(accountServiceMock.getBalance(Mockito.anyLong())).thenThrow(AccountNotFoundException.class);
		 
		 mockMvc.perform(get(HOST + API_VERSION + URI_MODULE + URI_BALANCE + "?id=1")
				 .contentType(MediaType.APPLICATION_JSON_UTF8)
				 .accept(MediaType.APPLICATION_JSON_UTF8))
		 		 .andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testGetBalanceOfExistingAccountWithParam() throws Exception {
		 when(accountServiceMock.getBalance(Mockito.anyLong())).thenReturn(new Double(10000.0));
		 
		 mockMvc.perform(get(HOST + API_VERSION + URI_MODULE + URI_BALANCE + "?id=1")
				 .contentType(MediaType.APPLICATION_JSON_UTF8)
				 .accept(MediaType.APPLICATION_JSON_UTF8))
		 		 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		 		 .andExpect(status().isOk());
	 }
	 
	 
	 //			AUTHENTICATION TESTS
	 
	 @Test
	 public void testAuthenticationWithIncorrectContentType() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
			 		.contentType(MediaType.APPLICATION_XML)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
			 		.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testAuthenticationWithWrongBody() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(WRONG_BODY))
			 		.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testAuthenticationeWrongBody() throws JsonProcessingException, Exception {		
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
				 		.contentType(MediaType.APPLICATION_JSON_UTF8)
				 		.accept(MediaType.APPLICATION_JSON_UTF8)
				 		.content(WRONG_BODY))
				 		.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testFailedAuthentication() throws JsonProcessingException, Exception {
		 when(accountServiceMock.getAccountByIdNPin(Mockito.anyLong(), Mockito.anyInt())).
		 thenThrow(AccountNotFoundException.class);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.credentials)))
			 		.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testCorrectAuthentication() throws JsonProcessingException, Exception {
		 when(accountServiceMock.getAccountByIdNPin(credentials.getAccountNumber(), credentials.getPin())).thenReturn(this.account);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_AUTH)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.credentials)))
		 			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			 		.andExpect(status().isOk());
	 }
	 
	 //			MAKE DEPOSIT TESTS
	 
	 @Test
	 public void testMakeDepositWithIncorrectContentType() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_DEPOSIT)
			 		.contentType(MediaType.APPLICATION_XML)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
			 		.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testMakeDepositWithIncorrectBody() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_DEPOSIT)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(WRONG_BODY))
			 		.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testDepositToNonExistentAccount() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_DEPOSIT)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
			 		.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testCorrectDeposit() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_DEPOSIT)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
		 			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			 		.andExpect(status().isOk());
	 }
	 
	 //			MAKE WITHDRAWAL TESTS
	 
	 @Test
	 public void testMakeWithdrawalWithIncorrectContentType() throws JsonProcessingException, Exception {
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_WITHDRAWAL)
			 		.contentType(MediaType.APPLICATION_XML)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
			 		.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testMakeWithdrawalWithWrongBody() throws JsonProcessingException, Exception {
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_WITHDRAWAL)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(WRONG_BODY))
			 		.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testMakeWithdrawalWithInsuficientFunds() throws JsonProcessingException, Exception {

		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_WITHDRAWAL)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
			 		.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testMakeWithdrawalToNonExistentAccount() throws JsonProcessingException, Exception {

		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_MAKE_WITHDRAWAL)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsString(this.transaction)))
			 		.andExpect(status().isUnprocessableEntity());
	 }
	 
	 //		PROCESS DEBITS AND CHECKS TESTS
	 
	 @Test
	 public void testProcessDebistNChecksWithIncorrectContentType() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_DEBITS_CHECKS)
			 		.contentType(MediaType.APPLICATION_XML)
			 		.accept(MediaType.APPLICATION_JSON_UTF8))
			 		.andExpect(status().isUnsupportedMediaType());
	 }
	 
	 @Test
	 public void testProcessDebistNChecksWithWrongBody() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_DEBITS_CHECKS)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(WRONG_BODY))
			 		.andExpect(status().isBadRequest());
	 }
	 
	 @Test
	 public void testProcessDebistNChecksWithInvalidData() throws JsonProcessingException, Exception {
		 this.transaction.setType(null);
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_DEBITS_CHECKS)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsBytes(this.transaction)))
			 		.andExpect(status().isUnprocessableEntity());
	 }
	 
	 @Test
	 public void testCorrectProcessDebistNChecks() throws JsonProcessingException, Exception {
		 
		 mockMvc.perform(post(HOST + API_VERSION + URI_MODULE + URI_DEBITS_CHECKS)
			 		.contentType(MediaType.APPLICATION_JSON_UTF8)
			 		.accept(MediaType.APPLICATION_JSON_UTF8)
			 		.content(mapper.writeValueAsBytes(this.transaction)))
			 		.andExpect(status().isOk());
	 }
	 
}
