package com.example.app.serviceImpl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.repository.AccountRepository;
import com.example.app.repository.TransactionRepository;
import com.example.app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	private final String NOT_FOUND = "Account not found by id ";

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public Account save(Account entity) {
		return this.accountRepository.save(entity);
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
					throw new OverdrawnAccountException("Account's balance negative, unable to close");
				}
			} else {
				throw new AccountNotFoundException(NOT_FOUND + id);
			}
	}
	
	@Override
	public double getBalance(Long id) throws AccountNotFoundException  {
		try {
			return accountRepository.findById(id).get().getBalance();
		} catch(NoSuchElementException e) {
			throw new AccountNotFoundException(NOT_FOUND + id);
		}
	}
	
	@Override
	public Account getAccountbyIdNPin(Long id, Integer pin) throws AccountNotFoundException {
		Account account = accountRepository.findByIdAndPin(id, pin);
		if(account != null) {
			return account;
		} else {
			throw new AccountNotFoundException(NOT_FOUND + id + " and PIN: " + pin);
		}
	}
	
	@Override
	public Account save(Transaction transaction) throws InsuficientFundsException, AccountNotFoundException  {
		Account lockedAccount = accountRepository.findAndLockById(transaction.getAccount().getId());
		
		if(lockedAccount != null ) {
			if(transaction.getType() == DEPOSIT) {
				transactionRepository.save(transaction);
				lockedAccount.setBalance(lockedAccount.getBalance() + transaction.getAmount());
				return lockedAccount;
			} 
			else if(lockedAccount.getBalance() >= transaction.getAmount()) {
				transactionRepository.save(transaction);
				lockedAccount.setBalance(lockedAccount.getBalance() - transaction.getAmount());
				return lockedAccount;
			} 
			else { 
				throw new InsuficientFundsException("operation unsuccessful, insuficient funds" + lockedAccount.getId());
			}
		} else {
			throw new AccountNotFoundException(NOT_FOUND + transaction.getAccount().getId());
		}
	}
}
