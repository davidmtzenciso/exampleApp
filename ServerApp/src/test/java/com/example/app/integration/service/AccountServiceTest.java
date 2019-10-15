package com.example.app.integration.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.InitAccount;
import com.example.app.model.Account;
import com.example.app.service.AccountService;

@RunWith(SpringRunner.class)
public class AccountServiceTest implements InitAccount {

	@Autowired
	private AccountService service;
	
	@Autowired
	private Account newAccount;
	
	private Account savedAccount;
	
	@Before
	public void init() {
		this.newAccount = this.initialize(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		this.newAccount.setFirstName(null);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		this.newAccount.setLastName(null);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		this.newAccount.setPin(null);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test
	public void testSaveWillAllPropertiesAccount() {
		this.savedAccount = service.createAccount(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
