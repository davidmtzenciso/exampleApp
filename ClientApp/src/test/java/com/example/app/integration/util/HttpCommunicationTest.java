package com.example.app.integration.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.app.conf.AppConfig;
import com.example.app.conf.DataInitialization;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.util.HttpCommunication;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class HttpCommunicationTest implements HttpCommunication, DataInitialization  {
	
	@Autowired
	private URLBuilder builder;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Credentials credentials;
	
	@Autowired
	private Account account;
	
	private Account accountSaved;
	
	@Autowired
	private Transaction transaction;
	
	private Transaction transactionSaved;
	
	private final String SKIP_BEFORE = "skip before";
	private final String SKIP_AFTER = "skip after";
	
	private static final Logger LOG = Logger.getLogger(HttpCommunication.class.getName());
	
	@BeforeEach
	public void init(TestInfo info) throws UnsupportedEncodingException, JsonProcessingException, IOException, InterruptedException {
		this.credentials = this.initialize(credentials);
		this.account = this.initialize(account);
		this.transaction = this.initialize(transaction, account);
		
		LOG.info("executing before...");
		if(!info.getTags().contains(SKIP_BEFORE)) {
			this.post(builder.createAccount(), 
					mapper.writeValueAsString(account), 
					response -> {
						if(response.getStatusLine().getStatusCode() == 200) {
							try {
								this.accountSaved = mapper.readValue(response.getEntity().getContent(), Account.class);
								LOG.info("created!");
							} catch (UnsupportedOperationException | IOException e) {
								LOG.severe("Error creating resource!");
								e.printStackTrace();
							}
						}
						else {
							LOG.severe("status code: " + response.getStatusLine().getStatusCode());
						}
					}, 
					error -> {
						LOG.severe("error creating resource: " + error.getMessage());
					}).close();
			
			Thread.sleep(1000);
		} else {
			LOG.info("skipping before!");
		}
	}
	
	@AfterEach
	public void clean(TestInfo info) throws IOException, InterruptedException {
		LOG.info("executing after..");
		
		if(!info.getTags().contains(SKIP_AFTER)) { 
			this.delete(builder.deleteAccount(this.accountSaved.getId()), 
					response -> {
						try {
							LOG.info(mapper.readValue(response.getEntity().getContent(), String.class));
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						LOG.info("status code = " + response.getStatusLine().getStatusCode());
					}, 
					error -> {
						LOG.severe(error.getMessage());
					}).close();

			Thread.sleep(1000);
			LOG.info("done!");
		} else {
			LOG.info("skipping after");
		}
	}
	
	@Test
	@Tag(SKIP_BEFORE)
	public void testCreateAccount() throws IOException, InterruptedException {
		this.post(builder.createAccount(), 
					mapper.writeValueAsString(account), 
					response -> {
						if(response.getStatusLine().getStatusCode() == 200) {
							try {
								this.accountSaved = mapper.readValue(response.getEntity().getContent(), Account.class);
								Assertions.assertNotNull(accountSaved.getId());
							} catch (UnsupportedOperationException | IOException e) {
								e.printStackTrace();
							}
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						LOG.severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
	
	@Test
	@Tag(SKIP_AFTER)
	public void testDeleteAccount() throws IOException, InterruptedException {
		
		this.delete(builder.deleteAccount(this.accountSaved.getId()) , 
					response -> {
						try {
							Logger.getGlobal().info(new StringBuilder().append(response.getEntity().getContent()).toString());
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						Logger.getGlobal().severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
		
	
	//@Test
	public void testAuthetication() throws IOException, InterruptedException {
		this.post(builder.authentication(), 
					mapper.writeValueAsString(credentials) , 
					response -> {
						try {
							Logger.getGlobal().info(new StringBuilder().append(response.getEntity().getContent()).toString());
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						Logger.getGlobal().severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
	

	//@Test
	public void testGetBalance() throws IOException, InterruptedException {
		this.get(builder.getBalance( new Long(1)), 
					response -> {
						try {
							Logger.getGlobal().info(new StringBuilder().append(response.getEntity().getContent()).toString());
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						Logger.getGlobal().severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
	
	//@Test
	public void testMakeDeposit() throws IOException, InterruptedException {
		this.account.setId(new Long(1));
		
		this.post(builder.makeDeposit(), 
					mapper.writeValueAsString(transaction) , 
					response -> {
						try {
							Logger.getGlobal().info(new StringBuilder().append(response.getEntity().getContent()).toString());
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						Logger.getGlobal().severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
	
	//@Test
	public void testMakeWithdrawal() throws IOException, InterruptedException {
		this.account.setId(new Long(1));
		
		this.post(builder.makeWithdrawal(), 
					mapper.writeValueAsString(transaction) , 
					response -> {
						try {
							Logger.getGlobal().info(new StringBuilder().append(response.getEntity().getContent()).toString());
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						Logger.getGlobal().severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
	
	//@Test
	public void testExternalDebitsNChecks() throws IOException, InterruptedException {
		this.post(builder.externalDebitsNChecks(), 
					mapper.writeValueAsString(transaction) , 
					response -> {
						try {
							Logger.getGlobal().info(new StringBuilder().append(response.getEntity().getContent()).toString());
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Assertions.assertTrue(response.getStatusLine().getStatusCode() == 200);
					}, 
					error -> {
						Logger.getGlobal().severe(error.getMessage());
					}).close();
		
		Thread.sleep(1000);
	}
}

