package com.example.app.integration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

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
	
	@Autowired
	private Transaction transaction;
	
	private Transaction transactionSaved;
	private Account accountSaved;
	private int status;
	private String responseMsg;
	
	private final String SKIP_BEFORE = "skipBefore";
	private final String SKIP_AFTER = "skipAfter";
	
	private static final Logger LOG = Logger.getLogger(HttpCommunication.class.getName());
	
	private String readMessage(HttpResponse response) throws UnsupportedOperationException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		reader.lines().forEach(line -> builder.append(line));
		reader.close();
		return builder.toString();
	}
	
	@BeforeEach
	public void init(TestInfo info) throws UnsupportedEncodingException, JsonProcessingException, IOException, InterruptedException {
		CloseableHttpAsyncClient client;
		
		this.credentials = this.initialize(credentials);
		this.account = this.initialize(account);
		this.transaction = this.initialize(transaction, account);
		status = 0;
		
		if(!info.getTags().contains(SKIP_BEFORE)) {
			LOG.info("executing before...");
			client = this.post(builder.createAccount(), 
					mapper.writeValueAsString(account), 
					response -> {
						if(response.getStatusLine().getStatusCode() == 200) {
							try {
								this.accountSaved = mapper.readValue(response.getEntity().getContent(), Account.class);
								LOG.info(this.accountSaved.getId() + "");
								LOG.info(this.accountSaved.getPin() + "");
							} catch (UnsupportedOperationException | IOException e) {
								LOG.severe("Error creating resource!");
								e.printStackTrace();
							}
						}
						else {
							LOG.severe("error, status code: " + response.getStatusLine().getStatusCode());
						}
					}, 
					error -> {
						LOG.severe("error creating resource: " + error.getMessage());
					});
			
			Thread.sleep(1000);
			client.close();
			LOG.info("done!");
		} else {
			LOG.info("skipping before!");
		}
	}
	
	@AfterEach
	public void clean(TestInfo info) throws IOException, InterruptedException {
		CloseableHttpAsyncClient client;
		
		if(!info.getTags().contains(SKIP_AFTER)) { 
			LOG.info("executing after..");
			if(this.accountSaved != null) {
				 client = this.delete(builder.deleteAccount(this.accountSaved.getId()), 
						response -> {
							try {
								LOG.info("DELETE RESPONSE: " + this.readMessage(response));
							} catch (UnsupportedOperationException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, 
						error -> {
							LOG.severe(error.getMessage());
						});
	
				Thread.sleep(1000);
				LOG.info("done!");
				client.close();
				this.accountSaved = null;
			}
		}
		else {
			LOG.info("skipping after");
		}
	}
	
	@Test
	@Tag(SKIP_BEFORE)
	public void testCreateAccount(TestInfo info) throws IOException, InterruptedException {
		CloseableHttpAsyncClient client = this.post(builder.createAccount(), 
					mapper.writeValueAsString(account), 
					response -> {
						status = response.getStatusLine().getStatusCode();
						if(response.getStatusLine().getStatusCode() == 200) {
							try {
								this.accountSaved = mapper.readValue(response.getEntity().getContent(), Account.class);
								responseMsg = "saved!";
							} catch (UnsupportedOperationException | IOException e) {
								status = 0;
							}
						} 
						else {
							try {
								responseMsg  = this.readMessage(response);
							} catch (UnsupportedOperationException | IOException e) {
								e.printStackTrace();
							}
						}
					}, 
					error -> {
						responseMsg = error.getMessage();
						status = 0;
					});
		
		Thread.sleep(1000);
		client.close();
		Assertions.assertEquals(200, status, responseMsg);
	}
	
	@Test
	@Tag(SKIP_AFTER)
	public void testDeleteAccount(TestInfo info) throws IOException, InterruptedException {		
		CloseableHttpAsyncClient client = 
				this.delete(builder.deleteAccount(this.accountSaved.getId()) , 
					response -> {
						try {
							status = response.getStatusLine().getStatusCode();
							responseMsg = this.readMessage(response);
						} catch (UnsupportedOperationException e) {
							status = 0;
						} catch (IOException e) {
							status = 0;
						}
					}, 
					error -> {
						responseMsg = error.getMessage();
						status = 0;
					});
		
		Thread.sleep(1000);
		client.close();
		Assertions.assertEquals(200, status, responseMsg);
	}
		
	
	@Test
	@DisplayName("test correct authentication")
	public void testAuthetication(TestInfo info) throws IOException, InterruptedException {
		LOG.info(info.getDisplayName());
		credentials.setAccountNum(this.accountSaved.getId());
		credentials.setPin(this.accountSaved.getPin());
		LOG.info(this.accountSaved.getId() + "");
		LOG.info(this.accountSaved.getPin() + "");
		
		CloseableHttpAsyncClient client =  this.post(builder.authentication(), 
					mapper.writeValueAsString(credentials), 
					response -> {
						status = response.getStatusLine().getStatusCode();
						if(status == 200) {
							try {
								this.accountSaved = mapper.readValue(response.getEntity().getContent(), Account.class);
								this.responseMsg = "autheticated!";
							} catch (UnsupportedOperationException e) {
								responseMsg = e.getMessage();
								status = 0;
							} catch (IOException e) {
								status = 0;
								responseMsg = e.getMessage();
							}
						}
						else {
							try {
								responseMsg = this.readMessage(response);
							} catch (UnsupportedOperationException | IOException e) {
								e.printStackTrace();
								status = 0;
							}
						}
					}, 
					error -> {
						responseMsg = error.getMessage();
						status = 0;
					});
		
		Thread.sleep(1000);
		client.close();
		Assertions.assertEquals(200, status, responseMsg);
	}
	

	@Test
	public void testGetBalance() throws IOException, InterruptedException {
		CloseableHttpAsyncClient client = this.get(builder.getBalance(this.accountSaved.getId()), 
					response -> {
						try {
							responseMsg = this.readMessage(response);
							status = response.getStatusLine().getStatusCode();
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
							status = 0;
						} catch (IOException e) {
							status = 0;
							e.printStackTrace();
						}
					}, 
					error -> {
						status = 0;
						responseMsg = error.getMessage();
					});
		
		Thread.sleep(1000);
		client.close();
		Assertions.assertEquals(200, status, responseMsg);
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

