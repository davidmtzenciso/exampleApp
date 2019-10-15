package com.example.app.unit.service;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.model.Account;
import com.example.app.repository.AccountRepository;
import com.example.app.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest  {

	@MockBean
	private AccountRepository repositoryMock;
	
	@Autowired
	private AccountService service;
	
	private Account newAccount;
	
	private Account savedAccount;
	
	@Test
	public void testFindByIdAndPinExistent() {
		when(repositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(new Account());
		Assert.assertNotNull(this.repositoryMock.findByIdAndPin(new Long(1), 1234));
	}
	
	@Test
	public void testFindByIdAndPinnonExistent() {
		when(repositoryMock.findByIdAndPin(new Long(1), 1234)).thenReturn(null);
		Assert.assertNull(this.repositoryMock.findByIdAndPin(new Long(1), 1234));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		when(repositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		when(repositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		when(repositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test
	public void testSaveWillAllPropertiesAccount() {
		when(repositoryMock.save(this.newAccount)).thenReturn(new Account());
		this.savedAccount = service.createAccount(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
