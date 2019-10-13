package exercise.com.app.unit.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import exercise.com.app.conf.InitAccount;
import exercise.com.app.model.Account;
import exercise.com.app.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest implements InitAccount {

	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private Account newAccount;
	
	private Account savedAccount;
	
	@Before
	public void init() {
		this.newAccount = this.initialize(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		this.newAccount.setFirstName(null);
		this.savedAccount = this.repository.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		this.newAccount.setLastName(null);
		this.savedAccount = this.repository.save(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		this.newAccount.setPin(null);
		this.savedAccount = this.repository.save(this.newAccount);
	}
	
	@Test
	public void testSaveWithAllProperties() {
		this.savedAccount = this.repository.save(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
