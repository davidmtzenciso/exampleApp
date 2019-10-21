package com.example.app.serviceImpl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.repository.AccountRepository;
import com.example.app.repository.TransactionRepository;
import com.example.app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	private final String NOT_FOUND = "Account not found";
	private final String OVERDRAWN = "Account's balance negative, unable to close";
	private final String INSUFICIENT_FUNDS = "Operation unsuccessful, insuficient funds";
	private final String LOGIN_FAILED = "Invalid account number or pin, please try again";
	private final String FAILED_VALIDATION = "Unabñle to open account, Invalid data or missing required fields";

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public Account save(Account entity) throws FailedEntityValidationException {
		entity.setId(null);
		try {
			return this.accountRepository.save(entity);
		} catch(DataIntegrityViolationException e) {
			throw new FailedEntityValidationException(FAILED_VALIDATION);
		}
	}
	
	@Override
	public String deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException {
			Account lockedAccount = accountRepository.findAndLockById(id);
			
			if(lockedAccount != null) {
				if(lockedAccount.getBalance() >= 0) {
					accountRepository.delete(lockedAccount);
					return "Account closed!";
				}
				else {
					throw new OverdrawnAccountException(OVERDRAWN);
				}
			} else {
				throw new AccountNotFoundException(NOT_FOUND);
			}
	}
	
	@Override
	public Double getBalance(Long id) throws AccountNotFoundException  {
		try {
			return accountRepository.findById(id).get().getBalance();
		} catch(NoSuchElementException e) {
			throw new AccountNotFoundException(NOT_FOUND);
		}
	}
	
	@Override
	public Account getAccountbyIdNPin(Long id, Integer pin) throws AccountNotFoundException {
		Account account = accountRepository.findByIdAndPin(id, pin);
		if(account != null) {
			return account;
		} else {
			throw new AccountNotFoundException(LOGIN_FAILED);
		}
	}
	
	@Override
	public Transaction makeDeposit(Transaction transaction) throws AccountNotFoundException {
		Account lockedAccount = accountRepository.findAndLockById(transaction.getAccount().getId());
		Transaction saved;
		
		if(lockedAccount != null) {
			saved = transactionRepository.save(transaction);
			lockedAccount.setBalance(lockedAccount.getBalance() + transaction.getAmount());
			return saved;
		} else { 
			throw new AccountNotFoundException(NOT_FOUND + transaction.getAccount().getId());
		}
	}
	
	@Override
	public Transaction makeWithdrawal(Transaction transaction) throws InsuficientFundsException, AccountNotFoundException  {
		Account lockedAccount = accountRepository.findAndLockById(transaction.getAccount().getId());
		Transaction saved;  
		
		if(lockedAccount != null ) {
			if(lockedAccount.getBalance() >= transaction.getAmount()) {
				saved = transactionRepository.save(transaction);
				lockedAccount.setBalance(lockedAccount.getBalance() - transaction.getAmount());
				return saved;
			} 
			else { 
				throw new InsuficientFundsException(INSUFICIENT_FUNDS + lockedAccount.getId());
			}
		} else {
			throw new AccountNotFoundException(NOT_FOUND + transaction.getAccount().getId());
		}
	}
}
