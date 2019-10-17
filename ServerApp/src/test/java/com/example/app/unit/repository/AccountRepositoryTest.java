package com.example.app.unit.repository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Account;
import com.example.app.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest implements DataInitialization {

	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private Account newAccount;
	
	private Account saved;
		
	@Before
	public void init() {
		this.newAccount = this.initialize(this.newAccount);
	}
	
	@After
	public void clean() {
		if(saved != null) {
			this.repository.deleteById(this.saved.getId());
		}
	}
	
	@Test
	public void testFindByIdAndPinExisting() {
		this.saved = this.repository.save(this.newAccount);
		Assert.assertNotNull(this.repository.findByIdAndPin(this.saved.getId(), this.saved.getPin()));
	}
	
	@Test
	public void testFindAndLockByIdWithUnlocked() {
		this.saved = this.repository.save(this.newAccount);
		Account lockedAccount = this.repository.findAndLockById(this.saved.getId());
		Assert.assertNotNull(lockedAccount);
	}
	
	public void testFindAndLockByIdWithlocked() {
		this.saved = this.repository.save(this.newAccount);
		Account lockedAccount = this.repository.findAndLockById(this.saved.getId());
		Assert.assertNotNull(lockedAccount);
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteNonExisting() {
		this.repository.deleteById(new Long(0));
	}
	
	public void testDeleteExisting() {
		this.saved = this.repository.save(this.newAccount);
		this.repository.deleteById(saved.getId());
		Assert.assertFalse(repository.findById(this.saved.getId()).isPresent());
	}
	
	@Test
	public void testFindbyIdAndPinOfNonExistent() {
		Assert.assertNull(this.repository.findByIdAndPin(new Long(0), 0));
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {		
		this.newAccount.setFirstName(null);
		this.saved = this.repository.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {		
		this.newAccount.setLastName(null);
		this.saved = this.repository.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {		
		this.newAccount.setPin(null);		
		this.saved = this.repository.save(this.newAccount);
	}
	
	@Test
	public void testSaveWithAllProperties() {
		this.saved = this.repository.save(this.newAccount);
		Assert.assertNotNull(saved);
	}
	
}
