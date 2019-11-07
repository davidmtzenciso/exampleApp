package com.example.app.unit.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.repository.AccountRepository;
import com.example.app.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest implements DataInitialization  {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private Transaction entity;

	@Autowired
	private Account account;
		
	@Before
	public void init() {
		Account saved; 
		
		this.account = this.initialize(account);
		this.entity = this.initialize(entity, account);
		saved = this.accountRepository.save(this.account);
		this.entity.setAccount(saved);
	}
	
	// 	####	SAVE TRANSACTION TESTS #####
	
	
	// 		DATE VALIDATIONS
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithNullDate() {
		this.entity.setDate(null);
		transactionRepository.save(entity);
	}
	
	@Test
	public void testSaveWithValidDate() {
		Assert.assertNotNull(this.transactionRepository.save(entity));
	}
	
	//			TYPE VALIDATIONS
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithInvalidMinType() {
		this.entity.setType(0);
		this.transactionRepository.save(entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithInvalidMaxType() {
		this.entity.setType(5);
		this.transactionRepository.save(entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithNullType() {
		this.entity.setType(null);
		this.transactionRepository.save(entity);
	}
	
	//			AMOUNT VALIDATIONS
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithInvalidMinAmount() {
		this.entity.setAmount(-1.0);
		this.transactionRepository.save(entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithNullAmount() {
		this.entity.setAmount(null);
		this.transactionRepository.save(entity);
	}
	
	//			DESCRIPTION VALIDATIONS
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithNullDescription() {
		this.entity.setDescription(null);
		this.transactionRepository.save(entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithEmptyDescription() {
		this.entity.setDescription("");
		this.transactionRepository.save(entity);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithInvalidMaxDescription() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i < 51; i++) {
			builder.append("a");
		}
		this.entity.setDescription(builder.toString());
		this.transactionRepository.save(entity);
	}
	
	//			ACCOUNT VALIDATIONS

	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithNonExistentAccount() {
		this.entity.getAccount().setId(new Long(1231241));
		this.transactionRepository.save(entity);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testSaveWithNullAccount() {
		this.entity.setAccount(null);
		this.transactionRepository.save(entity);
	}
	
	//		SAVE TRANSACTION WITH CORRECT ARGS
	
	@Test
	public void testSaveWithValidArgs() {
		this.transactionRepository.save(entity);
	}
	
	//		#### FIND BY ACCOUNT TESTS ####
	
	//		ACCOUNT VALIDATIONS
	
	@Test
	public void testFindByAccountWithNullAccount() {
		PageRequest firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
		Assert.assertEquals(0, this.transactionRepository.findByAccount(null, firstFiveByDate).get().getContent().size());
	}
	
	@Test
	public void testFindByAccountWithNonExistentAccount() {
		PageRequest firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
		this.account.setId(new Long(1231241));
		Assert.assertEquals(0, this.transactionRepository.findByAccount(this.account, firstFiveByDate).get().getContent().size());
	}
	
	@Test
	public void testFindByAccountWithExistentAccount() {
		PageRequest firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
		this.entity.setAccount(this.accountRepository.save(this.account));
		this.transactionRepository.save(entity);
		Assert.assertEquals(1, this.transactionRepository.findByAccount(entity.getAccount(), firstFiveByDate).get().getContent().size());
	}
	
	//			PAGE VALIDATIONS
	
	@Test
	public void testFindByAccountWithExistentAccountAndFirstFiveTransactions() {
		PageRequest firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
		Account saved = this.accountRepository.save(this.account);
		
		for(int i=0; i < 10; i++) {
			this.entity.setAccount(saved);
			this.transactionRepository.save(entity);
			entity.setDate(Date.valueOf(LocalDate.now()));
			entity.setId(null);
		}
		
		Assert.assertEquals(5, this.transactionRepository.findByAccount(entity.getAccount(), firstFiveByDate).get().getContent().size());
	}
	
	@Test
	public void testFindByAccountWithExistentAccountAndOrderedByDateFirstFiveTransactions() {
		PageRequest firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
		Account saved = this.accountRepository.save(this.account);
		List<Transaction> list;
		
		for(int i=0; i < 10; i++) {
			this.entity.setAccount(saved);
			this.transactionRepository.save(entity);
			entity.setDate(Date.valueOf("2019-1-"+(i+1)));
			entity.setId(null);
		}
		list = this.transactionRepository.findByAccount(entity.getAccount(), firstFiveByDate).get().getContent();
		Assert.assertTrue(list.get(1).getDate().after(list.get(0).getDate()));
	}
}

