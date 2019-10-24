package com.example.app.serviceImpl;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public Account save(Account entity) throws FailedEntityValidationException {
		entity.setId(null);
		
		try {
			 if(entity.getId() != null) {
				throw new FailedEntityValidationException(ID_DEFINED);
			 } else if(entity.getPin() == null) {
				throw new FailedEntityValidationException(PIN_CERO);
			 } else if(entity.getPin() <= 0) {
				 throw new FailedEntityValidationException(PIN_CERO);
			 } else if(entity.getFirstName() == null) {
				 throw new FailedEntityValidationException(EMPTY_FIRST_NAME);
			 } else if(entity.getFirstName().isEmpty()) {
				 throw new FailedEntityValidationException(EMPTY_FIRST_NAME);
			 } else if(entity.getLastName() == null) {
				 throw new FailedEntityValidationException(EMPTY_LAST_NAME);
			 } else if(entity.getLastName().isEmpty()) {
				 throw new FailedEntityValidationException(EMPTY_LAST_NAME);
			 }
			
			return this.accountRepository.save(entity);
		} catch(DataIntegrityViolationException e) {
			throw new FailedEntityValidationException(FAILED_VALIDATION);
		}
	}
	
	@Override
	public String deleteAccount(Long id) throws OverdrawnAccountException, AccountNotFoundException {
			int comparison;
			Account lockedAccount = accountRepository.findAndLockById(id);
			
			if(lockedAccount != null) {
				comparison = Double.compare(lockedAccount.getBalance(), 0.0);
				if(comparison >= 0) {
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
		Pageable firstFiveByDate;
		List<Transaction> list;
		
		if(account != null) {
			 firstFiveByDate = PageRequest.of(0, 5, Sort.by("date").ascending());
			list = this.transactionRepository.findByAccount(account, firstFiveByDate).getContent();
			list.forEach(transaction -> transaction.setAccount(null));
			account.setTransactions(list);
			return account;
		} else {
			throw new AccountNotFoundException(LOGIN_FAILED);
		}
	}
	
	@Override
	public Transaction makeDeposit(Transaction transaction) throws AccountNotFoundException, FailedEntityValidationException {
		Account lockedAccount;
		Transaction saved;
		
		if(transaction.getAccount() != null) { 
			lockedAccount = accountRepository.findAndLockById(transaction.getAccount().getId());
			if(lockedAccount != null) {
				try {
					saved = transactionRepository.save(transaction);
					lockedAccount.setBalance(lockedAccount.getBalance() + transaction.getAmount());
					this.accountRepository.flush();
					saved.setAccount(lockedAccount);
					lockedAccount.setTransactions(null);
				} catch(DataIntegrityViolationException e) {
					throw new FailedEntityValidationException(INVALID_TRANSACTION);
				}
				return saved;
			} else { 
				throw new AccountNotFoundException(NOT_FOUND);
			}
		} else {
			throw new FailedEntityValidationException(ACCOUNT_NOT_PRESENT);
		}
	}
	
	@Override
	public Transaction makeWithdrawal(Transaction transaction) throws InsuficientFundsException, AccountNotFoundException, FailedEntityValidationException  {
		Account lockedAccount;
		Transaction saved;  
		int comparison;
		
		if(transaction.getAccount() != null) {
			lockedAccount = accountRepository.findAndLockById(transaction.getAccount().getId());
			if(lockedAccount != null ) {
				comparison = Double.compare(lockedAccount.getBalance(), transaction.getAmount());
				if(comparison >= 0) {
					try {
						saved = transactionRepository.save(transaction);
						lockedAccount.setBalance(lockedAccount.getBalance() - transaction.getAmount());
						this.accountRepository.flush();
						lockedAccount.setTransactions(null);
						saved.setAccount(lockedAccount);
						return saved;
					} catch(DataIntegrityViolationException e) {
						throw new FailedEntityValidationException(INVALID_TRANSACTION);
					}
				} 
				else { 
					throw new InsuficientFundsException(INSUFICIENT_FUNDS + lockedAccount.getId());
				}
			} else {
				throw new AccountNotFoundException(NOT_FOUND);
			}
		} else {
			throw new FailedEntityValidationException(ACCOUNT_NOT_PRESENT);
		}
	}
}
