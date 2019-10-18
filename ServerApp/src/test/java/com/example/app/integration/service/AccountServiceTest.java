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
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
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
	public void testDeleteNonOverdrawalExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		this.savedAccount = service.save(this.newAccount);
		Assert.assertNotNull(service.deleteAccount(this.savedAccount.getId()));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testDeleteNonExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		Assert.assertNotNull(service.deleteAccount(new Long(1)));
	}
	
	@Test(expected = OverdrawnAccountException.class)
	public void testDeleteOverdrawalExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		this.newAccount.setBalance(-10.0);
		this.savedAccount = service.save(this.newAccount);
		service.deleteAccount(this.savedAccount.getId());
	}
	
	@Test
	public void testSaveTransactionCorrectAmount() throws InsuficientFundsException, AccountNotFoundException {
		this.savedAccount = this.service.save(this.newAccount);
		this.transaction = this.initialize(this.transaction, this.savedAccount);
		Assert.assertNotNull(this.service.save(transaction));
	}
	
	@Test(expected = InsuficientFundsException.class)
	public void testSaveTransactionIncorrectAmount() throws InsuficientFundsException, AccountNotFoundException {
		this.savedAccount = this.service.save(this.newAccount);
		this.transaction = this.initialize(this.transaction, this.savedAccount);
		this.transaction.setAmount(2000.0);
		this.service.save(transaction);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testSaveTransactionWithNonExistentAccount() throws InsuficientFundsException, AccountNotFoundException {
		this.newAccount.setId(new Long(1));
		this.transaction = this.initialize(this.transaction, this.newAccount);
		this.service.save(transaction);
	}
	
	@Test
	public void testGetBalanceExistingAccount() throws AccountNotFoundException {
		this.savedAccount = this.service.save(this.newAccount);
		Assert.assertNotNull(this.service.getBalance(this.savedAccount.getId()));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testGetBalanceNonExistingAccount() throws AccountNotFoundException {
		Assert.assertNotNull(this.service.getBalance(new Long(1)));
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
