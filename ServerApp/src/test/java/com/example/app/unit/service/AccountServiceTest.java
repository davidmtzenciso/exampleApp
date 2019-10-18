package com.example.app.unit.service;

import static org.mockito.Mockito.when;

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
	private Account newAccount;
	
	@Autowired
	private Transaction transaction;
	
	private Account savedAccount;
	
	@Before
	public void init() {
		this.newAccount = this.initialize(this.newAccount);
		this.transaction = this.initialize(this.transaction, this.newAccount);
	}
	
	@Test
	public void testDeleteNonOverdrawnExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.newAccount);
		this.service.deleteAccount(new Long(1));
	}
	
	@Test(expected = OverdrawnAccountException.class)
	public void testDeleteOverdrawnExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		this.newAccount.setBalance(-10.0);
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.newAccount);
		this.service.deleteAccount(new Long(1));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testDeleteNonExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(null);
		this.service.deleteAccount(new Long(1));
	}
		
	@Test
	public void saveTransactionWithCorrectAmount() throws InsuficientFundsException, AccountNotFoundException {
		this.newAccount.setId(new Long(1));
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.newAccount);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		Assert.assertTrue(service.save(this.transaction).getBalance() >= 0);
	}
	
	@Test(expected = InsuficientFundsException.class)
	public void saveTransactionWithIncorrectAmount() throws InsuficientFundsException, AccountNotFoundException {
		this.newAccount.setId(new Long(1));
		this.transaction.setAmount(2000.0);
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.newAccount);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		this.service.save(this.transaction);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void saveTransactionWithNonExistentAccount() throws InsuficientFundsException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(null);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		this.service.save(this.transaction);
	}
	
	@Test
	public void testFindByIdAndPinExistent() throws AccountNotFoundException {
		when(accountRepositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(this.newAccount);
		Assert.assertNotNull(this.service.getAccountbyIdNPin(new Long(1), 1234));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testFindByIdAndPinNonExistent() throws AccountNotFoundException {
		when(accountRepositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(null);
		this.service.getAccountbyIdNPin(new Long(1), 1234);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		when(accountRepositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		when(accountRepositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		when(accountRepositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.save(this.newAccount);
	}
	
	@Test
	public void testSaveWithAllProperties() {
		when(accountRepositoryMock.save(this.newAccount)).thenReturn(this.newAccount);
		this.savedAccount = service.save(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
