package exercise.com.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exercise.com.app.model.Account;
import exercise.com.app.repository.AccountRepository;
import exercise.com.app.service.AccountService;

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
