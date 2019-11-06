package com.example.app.serviceImpl;


import java.util.NoSuchElementException;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsufficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.repository.AccountRepository;
import com.example.app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository repository;
	
	@Override
	public Account save(Account entity) throws FailedEntityValidationException {
		try {
			return this.repository.save(entity);
		} catch(DataIntegrityViolationException e) {
			throw new FailedEntityValidationException(FAILED_VALIDATION);
		}
	}
	
	@Override
	public void deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException {
			Account lockedAccount;
			
			try {
				lockedAccount = repository.findAndLockById(id).get();
				if(lockedAccount.getBalance() >= 0) {
					repository.delete(lockedAccount);
				}
				else {
					throw new OverdrawnAccountException(OVERDRAWN);
				}
			} catch(NoSuchElementException e) {
				throw new AccountNotFoundException(NOT_FOUND);
			}
	}
	
	@Override
	public Double getBalance(Long id) throws AccountNotFoundException  {
		try {
			return repository.findById(id).get().getBalance();
		} catch(NoSuchElementException e) {
			throw new AccountNotFoundException(NOT_FOUND);
		}
	}
	
	@Override
	public Account getAccountbyIdNPin(Long id, Integer pin) throws AccountNotFoundException {
		try {
			return repository.findByIdAndPin(id, pin).get();
		} catch(NoSuchElementException e) {
			throw new AccountNotFoundException(LOGIN_FAILED);
		}
	}
	
	@Override
	public void addAmount(Double amount, long id) throws AccountNotFoundException {
		Account lockedAccount;
		
		try {
			lockedAccount = this.repository.findAndLockById(id).get();
			lockedAccount.setBalance(lockedAccount.getBalance() + amount);
			this.repository.flush();
		} catch(NoSuchElementException e) {
			throw new AccountNotFoundException(NOT_FOUND);
		}
	}
	
	@Override
	public void extractAmount(Double amount, long id) throws InsufficientFundsException, AccountNotFoundException {
		Account lockedAccount;
		
		try {
			lockedAccount = this.repository.findAndLockById(id).get();
			if(lockedAccount.getBalance() >= amount) {
				lockedAccount.setBalance(lockedAccount.getBalance() - amount);
				this.repository.flush();
			} else {
				throw new InsufficientFundsException(INSUFICIENT_FUNDS);
			}
		} catch(NoSuchElementException e) {
			throw new AccountNotFoundException(NOT_FOUND);
		}
	}
}
