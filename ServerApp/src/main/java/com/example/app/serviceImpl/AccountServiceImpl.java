package com.example.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.Account;
import com.example.app.repository.AccountRepository;
import com.example.app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository repository;
	
	@Override
	public Account createAccount(Account entity) {
		return this.repository.save(entity);
	}
	
	@Override
	public Account getAccountbyIdNPin(Long id, Integer pin) {
		return repository.findByIdAndPin(id, pin);
	}
}
