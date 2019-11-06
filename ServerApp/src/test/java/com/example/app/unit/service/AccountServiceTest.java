package com.example.app.unit.service;


import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.repository.AccountRepository;
import com.example.app.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest implements DataInitialization  {

	@MockBean
	private AccountRepository accountRepositoryMock;
	
	@Autowired
	private AccountService service;
	
	@Autowired
	private Account account;
	
	private Account savedAccount;
	
	@Before
	public void init() {
		this.account = this.initialize(this.account);
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
		when(accountRepositoryMock.findAndLockById(new Long(1)).get()).thenReturn(this.account);
		this.service.deleteAccount(new Long(1));
	}
	
	@Test(expected = OverdrawnAccountException.class)
	public void testDeleteOverdrawnExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		this.account.setBalance(-10.0);
		when(accountRepositoryMock.findAndLockById(Mockito.anyLong()).get()).thenReturn(this.account);
		this.service.deleteAccount(new Long(1));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testDeleteNonExistingAccount() throws OverdrawnAccountException, AccountNotFoundException {
		when(accountRepositoryMock.findAndLockById(Mockito.anyLong())).thenReturn(null);
		this.service.deleteAccount(new Long(1));
	}
	
	// 		GET ACCOUNT BALANCE TESTS
	
	@Test(expected = AccountNotFoundException.class)
	public void testGetBalanceOfNonExistingAccount() throws AccountNotFoundException {
		when(accountRepositoryMock.findById(Mockito.anyLong())).thenThrow(NoSuchElementException.class);
		this.service.getBalance(new Long(1));
	}
	
	@Test
	public void testgetBalanceOfExistingAccount() throws AccountNotFoundException {
		when(accountRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(this.account));
		Assert.assertNotNull(this.service.getBalance(Mockito.anyLong()));
	}
	
	
	//		 GET ACCOUNT BY ID AND PIN TESTS
	
	@Test
	public void testFindByIdAndPinExistent() throws AccountNotFoundException {		
		when(accountRepositoryMock.findByIdAndPin(Mockito.anyLong(), Mockito.anyInt()).get()).thenReturn(this.account);
		Assert.assertNotNull(this.service.getAccountbyIdNPin(Mockito.anyLong(), Mockito.anyInt()));
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void testFindByIdAndPinNonExistent() throws AccountNotFoundException {
		when(accountRepositoryMock.findByIdAndPin(Mockito.anyLong(), Mockito.anyInt()).get()).thenThrow(AccountNotFoundException.class);
		this.service.getAccountbyIdNPin(new Long(1), 1234);
	}

}