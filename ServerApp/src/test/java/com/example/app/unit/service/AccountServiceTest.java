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
import com.example.app.exception.InsuficientFundsException;
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
	public void saveTransactionWithCorrectAmount() throws InsuficientFundsException {
		this.newAccount.setId(new Long(1));
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.newAccount);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		Assert.assertTrue(service.saveTransaction(this.transaction).getBalance() >= 0);
	}
	
	@Test(expected = InsuficientFundsException.class)
	public void saveTransactionWithInCorrectAmount() throws InsuficientFundsException {
		this.newAccount.setId(new Long(1));
		this.transaction.setAmount(2000.0);
		when(accountRepositoryMock.findAndLockById(new Long(1))).thenReturn(this.newAccount);
		when(transactionRepositoryMock.save(this.transaction)).thenReturn(this.transaction);
		this.service.saveTransaction(this.transaction);
	}
	
	@Test
	public void testFindByIdAndPinExistent() {
		when(accountRepositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(this.newAccount);
		Assert.assertNotNull(this.service.getAccountbyIdNPin(new Long(1), 1234));
	}
	
	@Test
	public void testFindByIdAndPinNonExistent() {
		when(accountRepositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(null);
		Assert.assertNull(this.service.getAccountbyIdNPin(new Long(1), 1234));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		when(accountRepositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		when(accountRepositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		when(accountRepositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test
	public void testSaveWillAllPropertiesAccount() {
		when(accountRepositoryMock.save(this.newAccount)).thenReturn(new Account());
		this.savedAccount = service.createAccount(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
