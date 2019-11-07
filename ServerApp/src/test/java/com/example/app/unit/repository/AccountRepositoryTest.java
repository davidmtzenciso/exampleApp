package com.example.app.unit.repository;

import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	private Account entity;
				
	@Before
	public void init() {
		this.entity = this.initialize(this.entity);
	}
	
	//			#### SAVE ACCOUNT TESTS #####
	
	
	// 		FIRST NAME VALIDATIONS
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithNullFirstName() {		
		this.entity.setFirstName(null);
		this.repository.save(this.entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithEmptyFirstName() {		
		this.entity.setFirstName("");
		this.repository.save(this.entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithInvalidSizeFirstName() {		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i < 51; i++) {
			builder.append("a");
		}
		entity.setFirstName(builder.toString());
		this.repository.save(this.entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithoutLastName() {		
		this.entity.setLastName(null);
		 this.repository.save(this.entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithoutPin() {		
		this.entity.setPin(null);		
		this.repository.save(this.entity);
	}
	
	//		FIND BY ID AND PIN TESTS
	
	@Test
	public void testFindByIdAndPinWithCorrectIdAndIncorrectPin() {
		Account saved = this.repository.save(this.entity);
		Assert.assertFalse(this.repository.findByIdAndPin(saved.getId(), 1111).isPresent());
	}
	
	@Test
	public void testFindByIdAndPinWithIncorrectIdAndCorrentPin() {
		Account saved = this.repository.save(this.entity);
		Assert.assertFalse(this.repository.findByIdAndPin(new Long(1231432), saved.getPin()).isPresent());
	}
	
	@Test
	public void testFindByIdAndPinWithIncorrectIdAndPin() {
		Assert.assertFalse(this.repository.findByIdAndPin(new Long(1231432), 1111).isPresent());
	}
	
	@Test
	public void testFindByIdAndPinWithCorrectIdAndPin() {
		Account saved = this.repository.save(this.entity);
		Assert.assertFalse(this.repository.findByIdAndPin(saved.getId(), saved.getPin()).isPresent());
	}
	
	//		FIND AND LOCK BY ID
	
	@Test(expected = NoSuchElementException.class)
	public void testFindAndLockByIdWithIncorrectId() {
		Assert.assertTrue(this.repository.findAndLockById(new Long(112312312)).isPresent());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testFindAndLockByIdWithlocked() {
		Account saved = this.repository.save(this.entity);
		this.repository.findAndLockById(saved.getId()).get();
	}
	
	@Test
	public void testFindAndLockByIdWithCorrectIdAndUnlocked() {
		Account saved = this.repository.save(this.entity);
		Assert.assertTrue(this.repository.findAndLockById(saved.getId()).isPresent());
	}
	
	// 		DELETE ACCOUNT TESTS
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteWithIncorrectId() {
		this.repository.deleteById(new Long(1234233123));
	}
	
	@Test
	public void testDeleteWithCorrectId() {
		Account saved = this.repository.save(this.entity);
		this.repository.deleteById(saved.getId());
	}
	
}
