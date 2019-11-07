package com.example.app.unit.repository;

import java.util.NoSuchElementException;

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
	
	//			SAVE ACCOUNT TESTS
	
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
	
	//		FIND BY ID AND PIN TESTS
	
	@Test
	public void testFindByIdAndPinWithCorrectIdAndIncorrectPin() {
		this.saved = this.repository.save(this.newAccount);
		Assert.assertFalse(this.repository.findByIdAndPin(this.saved.getId(), 1111).isPresent());
	}
	
	@Test
	public void testFindByIdAndPinWithIncorrectIdAndCorrentPin() {
		this.saved = this.repository.save(this.newAccount);
		Assert.assertFalse(this.repository.findByIdAndPin(new Long(1231432), this.saved.getPin()).isPresent());
	}
	
	@Test
	public void testFindByIdAndPinWithIncorrectIdAndPin() {
		Assert.assertFalse(this.repository.findByIdAndPin(new Long(1231432), 1111).isPresent());
	}
	
	@Test
	public void testFindByIdAndPinWithCorrectIdAndPin() {
		this.saved = this.repository.save(this.newAccount);
		Assert.assertFalse(this.repository.findByIdAndPin(this.saved.getId(), this.saved.getPin()).isPresent());
	}
	
	//		FIND AND LOCK BY ID
	
	@Test(expected = NoSuchElementException.class)
	public void testFindAndLockByIdWithIncorrectId() {
		Assert.assertTrue(this.repository.findAndLockById(new Long(112312312)).isPresent());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testFindAndLockByIdWithlocked() {
		this.saved = this.repository.save(this.newAccount);
		this.repository.findAndLockById(this.saved.getId()).get();
	}
	
	@Test
	public void testFindAndLockByIdWithCorrectIdAndUnlocked() {
		this.saved = this.repository.save(this.newAccount);
		Assert.assertTrue(this.repository.findAndLockById(this.saved.getId()).isPresent());
	}
	
	// 		DELETE ACCOUNT TESTS
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteWithIncorrectId() {
		this.repository.deleteById(new Long(1234233123));
	}
	
	@Test
	public void testDeleteWithCorrectId() {
		this.saved = this.repository.save(this.newAccount);
		this.repository.deleteById(this.saved.getId());
	}
	
}
