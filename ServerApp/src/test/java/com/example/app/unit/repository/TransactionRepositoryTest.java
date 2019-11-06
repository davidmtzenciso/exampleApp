package com.example.app.unit.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
	private Transaction transaction;
	
	@Autowired
	private Account account;
	
	@Before
	public void init() {
		this.account = this.initialize(account);
		this.transaction = this.initialize(transaction, account);
	}
	
	// SAVE TRANSACTION TESTS
	
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void testSavetransactionWithNonExistentAccount() {
		this.transactionRepository.save(transaction);
	}
	
	
}

