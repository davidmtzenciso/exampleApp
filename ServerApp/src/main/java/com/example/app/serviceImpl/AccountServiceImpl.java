package com.example.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.exception.InsuficientFundsException;
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
	public Account createAccount(Account entity) {
		return this.accountRepository.save(entity);
	}
	
	@Override
	public Account getAccountbyIdNPin(Long id, Integer pin) {
		return accountRepository.findByIdAndPin(id, pin);
	}
	
	@Override
	public Account findAndLockById(Long id) {
		return accountRepository.findAndLockById(id);
	}
	
	@Override
	public Account saveTransaction(Transaction transaction) throws InsuficientFundsException  {
		Account lockedAccount = accountRepository.findAndLockById(transaction.getAccount().getId());
		if(lockedAccount.getBalance() >= transaction.getAmount()) {
			transactionRepository.save(transaction);
			lockedAccount.setBalance(lockedAccount.getBalance() - transaction.getAmount());
			return lockedAccount;
		} else { 
			throw new InsuficientFundsException("operation unsuccessful, insuficient funds" + lockedAccount.getId());
		}
	}
}
