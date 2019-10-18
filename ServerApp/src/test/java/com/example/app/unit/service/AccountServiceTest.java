package com.example.app.unit.service;

import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.repository.AccountRepository;
import com.example.app.repository.TransactionRepository;
import com.example.app.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest implements DataInitialization  {

	@MockBean
	private AccountRepository accountRepositoryMock;
	
	@MockBean
	private TransactionRepository transactionRepositoryMock;
	
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
	
	// 		SAVE ACCOUNT TEST
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithoutFirstName() throws FailedEntityValidationException {
		when(accountRepositoryMock.save(this.account)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithoutLastName() throws FailedEntityValidationException {
		when(accountRepositoryMock.save(this.account)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.save(this.account);
	}
	
	@Test(expected = FailedEntityValidationException.class)
	public void testSaveWithoutPin() throws FailedEntityValidationException {
		when(accountRepositoryMock.save(this.account)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.save(this.account);
	}
	
	@Test
	public void testSaveWithAllProperties() throws FailedEntityValidationException {
		when(accountRepositoryMock.save(this.account)).thenReturn(this.account);
		this.savedAccount = service.save(this.account);
		Assert.assertNotNull(this.savedAccount);
	}
	
	// 		DELETE ACCOUNT TEST
	
	@Test
	public void testDeleteNonOverdrawnExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.account);
		this.service.deleteAccount(new Long(1));
	}
	
	@Test(expected = OverdrawnAccountException.class)
	public void testDeleteOverdrawnExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		this.account.setBalance(-10.0);
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.account);
		this.service.deleteAccount(new Long(1));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testDeleteNonExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(null);
		this.service.deleteAccount(new Long(1));
	}
	
	// 		GET ACCOUNT BALANCE TESTS
	
	@Test(expected = AccountNotFoundException.class)
	public void testGetBalanceOfNonExistingAccount() throws AccountNotFoundException {
		when(accountRepositoryMock.findById(new Long(1))).thenThrow(NoSuchElementException.class);
		this.service.getBalance(new Long(1));
	}
	
	@Test
	public void testgetBalanceOfExistingAccount() throws AccountNotFoundException {
		when(accountRepositoryMock.findById(new Long(1))).thenReturn(Optional.of(this.account));
		Assert.assertNotNull(this.service.getBalance(new Long(1)));
	}
	
	
	//		 GET ACCOUNT BY ID AND PIN TESTS
	
	@Test
	public void testFindByIdAndPinExistent() throws AccountNotFoundException {
		when(accountRepositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(this.account);
		Assert.assertNotNull(this.service.getAccountbyIdNPin(new Long(1), 1234));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testFindByIdAndPinNonExistent() throws AccountNotFoundException {
		when(accountRepositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(null);
		this.service.getAccountbyIdNPin(new Long(1), 1234);
	}

	// 		 MAKE DEPOSIT TESTS
	
	@Test(expected = AccountNotFoundException.class)
	public void testMakeDepositInNonExistentAccount() throws AccountNotFoundException {
		transaction.getAccount().setId(new Long(1));
		
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(null);
		service.makeDeposit(this.transaction);
	}
	
	@Test
	public void testMakeDepositInExistingAccount() throws AccountNotFoundException {
		transaction.getAccount().setId(new Long(1));
		
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.account);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		Assert.assertNotNull(service.makeDeposit(transaction));
	}
		
	//		 MAKE WITHDRAWAL TESTS
	
	@Test
	public void testMakeWithdrawalWithCorrectAmount() throws InsuficientFundsException, AccountNotFoundException {
		this.account.setId(new Long(1));
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.account);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		Assert.assertNotNull(service.makeWithdrawal(this.transaction));
	}
	
	@Test(expected = InsuficientFundsException.class)
	public void testMakeWithdrawalWithIncorrectAmount() throws InsuficientFundsException, AccountNotFoundException {
		this.account.setId(new Long(1));
		this.transaction.setAmount(2000.0);
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.account);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		this.service.makeWithdrawal(this.transaction);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void saveTransactionWithNonExistentAccount() throws InsuficientFundsException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(null);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		this.service.makeWithdrawal(this.transaction);
	}
}
