package com.example.app.unit.service;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsufficientFundsException;
import com.example.app.model.Transaction;
import com.example.app.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

	@MockBean
	private TransactionRepository transactionRepositoryMock;
	
	@Autowired
	private Transaction transaction;
	
	@Before
	public void init() {
		this.transaction = this.initialize(this.transaction, this.account);

	}
	
}
