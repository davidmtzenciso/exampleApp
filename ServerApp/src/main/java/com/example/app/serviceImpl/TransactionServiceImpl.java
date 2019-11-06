package com.example.app.serviceImpl;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.TransactionsNotFoundException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.repository.TransactionRepository;
import com.example.app.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceImpl.class);
		
	@Override
	public Transaction save(Transaction transaction) throws FailedEntityValidationException {
		try {
			return transactionRepository.save(transaction);
		} catch(DataIntegrityViolationException e) {
			throw new FailedEntityValidationException(INVALID_TRANSACTION);
		}
	}

	@Override
	public List<Transaction> getlastFive(Account account) throws TransactionsNotFoundException  {
		try {
			PageRequest firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
			return this.transactionRepository.findByAccount(account, firstFiveByDate).get().getContent();
		}catch(NoSuchElementException e) {
			LOG.warn("transactions not found for account number: " + account.getId(), e);
			throw new TransactionsNotFoundException(NOT_FOUND);
		}
	}
}
