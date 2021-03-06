package com.example.app.integration.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsufficientFundsException;
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
	private Account account;
	
	@Autowired
	private Transaction transaction;
	
	private Account savedAccount;
	
	@Before
	public void init() {
		this.account = this.initialize(this.account);
		this.transaction = this.initialize(this.transaction, this.account);
	}
	
	//		SAVE ACCOUNT TESTS
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithtFirstNameNull() throws FailedEntityValidationException {
		this.account.setFirstName(null);
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithtFirstNameEmpty() throws FailedEntityValidationException {
		this.account.setFirstName("");
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithLastNameNull() throws FailedEntityValidationException {
		this.account.setLastName(null);
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithLastNameEmpty() throws FailedEntityValidationException {
		this.account.setLastName("");
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithoutPin() throws FailedEntityValidationException {
		this.account.setPin(null);
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithPinCero() throws FailedEntityValidationException {
		this.account.setPin(0);
		this.savedAccount = service.save(this.account);
	}
	
	@Test
	public void testSaveWillAllPropertiesAccount() throws FailedEntityValidationException {
		this.savedAccount = service.save(this.account);
		Assert.assertNotNull(this.savedAccount);
	}
	
	//		DELETE ACCOUNT TESTS
	
	@Test
	public void testDeleteNonOverdrawalExistingAccount() throws OverdrawnAccountException, AccountNotFoundException, FailedEntityValidationException {
		this.savedAccount = service.save(this.account);
		
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testDeleteNonExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
	}
	
	@Test(expected = OverdrawnAccountException.class)
	public void testDeleteOverdrawalExistingAccount() throws OverdrawnAccountException, AccountNotFoundException, FailedEntityValidationException {
		this.account.setBalance(-10.0);
		this.savedAccount = service.save(this.account);
		
		service.deleteAccount(this.savedAccount.getId());
	}
	
	//		GET ACCOUNT BALANCE TESTS
	
	@Test
	public void testGetBalanceExistingAccount() throws AccountNotFoundException, FailedEntityValidationException {
		this.savedAccount = this.service.save(this.account);
		Assert.assertNotNull(this.service.getBalance(this.savedAccount.getId()));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testGetBalanceNonExistingAccount() throws AccountNotFoundException {
		Assert.assertNotNull(this.service.getBalance(new Long(1323423423)));
	}
	
	//		GET ACCOUNT BY ID AND PIN TEST
	
	@Test
	public void testGetAccountbyIdNPinExistentWithFirstFive() throws AccountNotFoundException, FailedEntityValidationException {
		Account account = this.service.getAccountByIdNPin(new Long(1), 1234);
		Assert.assertEquals(5, account.getTransactions().size());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testGetAccountbyIdNPinNonExistent() throws AccountNotFoundException, FailedEntityValidationException {		
		this.service.getAccountByIdNPin(new Long(1252153452), 1234);
	}
	
	
	// 		MAKE DEPOSIT TESTS
	
	@Test
	public void testMakeDepositInExistentAccount() throws FailedEntityValidationException, AccountNotFoundException {
		this.savedAccount = this.service.save(this.account);
		
		this.transaction.setAccount(this.savedAccount);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testMakeDepositInNonExistentAccount() throws FailedEntityValidationException, AccountNotFoundException {		
		this.transaction.getAccount().setId(new Long(235235231));
	}
	
	//		 MAKE WITHDRAWAL TESTS
	
	@Test
	public void testMakewithdrawalCorrectAmount() throws InsufficientFundsException, AccountNotFoundException, FailedEntityValidationException {
		this.savedAccount = this.service.save(this.account);
		this.transaction.setAccount(this.savedAccount);
		
	}
	
	@Test(expected = InsufficientFundsException.class)
	public void testMakeWithdrawalIncorrectAmount() throws InsufficientFundsException, AccountNotFoundException, FailedEntityValidationException {
		this.savedAccount = this.service.save(this.account);
		this.transaction.setAccount(this.savedAccount);
		this.transaction.setAmount(2000.0);
		
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testMakeWithdreawalWithNonExistentAccount() throws InsufficientFundsException, AccountNotFoundException, FailedEntityValidationException {
		this.account.setId(new Long(1346272436));
	}
	
}
