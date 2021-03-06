package com.example.app.unit.service;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.conf.DataInitialization;
import com.example.app.model.Transaction;
import com.example.app.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest implements DataInitialization {

	@MockBean
	private TransactionRepository transactionRepositoryMock;
	
	@Autowired
	private Transaction transaction;

	
	@Before
	public void init() {
		this.transaction = this.initialize(transaction, null);

	}
	
}
