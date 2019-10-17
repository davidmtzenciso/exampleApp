package com.example.app.integration.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest implements DataInitialization {

	@Autowired
	private AccountService service;
	
	@Autowired
	private Account newAccount;
	
	@Autowired
	private Transaction transaction;
	
	private Account savedAccount;
	
	
	@Before
	public void init() {
		this.newAccount = this.initialize(this.newAccount);
	}
	
	@Test
	public void testSaveTransactionCorrectAmount() throws InsuficientFundsException {
		this.savedAccount = this.service.save(this.newAccount);
		this.transaction = this.initialize(this.transaction, this.savedAccount);
		Assert.assertNotNull(this.service.save(transaction));
	}
	
	@Test(expected = InsuficientFundsException.class)
	public void testSaveTransactionIncorrectAmount() throws InsuficientFundsException {
		this.savedAccount = this.service.save(this.newAccount);
		this.transaction = this.initialize(this.transaction, this.savedAccount);
		this.transaction.setAmount(2000.0);
		this.service.save(transaction);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		this.newAccount.setFirstName(null);
		this.savedAccount = service.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		this.newAccount.setLastName(null);
		this.savedAccount = service.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		this.newAccount.setPin(null);
		this.savedAccount = service.save(this.newAccount);
	}
	
	@Test
	public void testSaveWillAllPropertiesAccount() {
		this.savedAccount = service.save(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
