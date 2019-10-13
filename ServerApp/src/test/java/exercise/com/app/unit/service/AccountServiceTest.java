package exercise.com.app.unit.service;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import exercise.com.app.model.Account;
import exercise.com.app.repository.AccountRepository;
import exercise.com.app.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest  {

	@MockBean
	private AccountRepository repositoryMock;
	
	@Autowired
	private AccountService service;
	
	private Account newAccount;
	
	private Account savedAccount;
	

	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutFirstName() {
		when(repositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutLastName() {
		when(repositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testSaveWithoutPin() {
		when(repositoryMock.save(this.newAccount)).thenThrow(DataIntegrityViolationException.class);
		this.savedAccount = service.createAccount(this.newAccount);
	}
	
	@Test
	public void testSaveWillAllPropertiesAccount() {
		when(repositoryMock.save(this.newAccount)).thenReturn(new Account());
		this.savedAccount = service.createAccount(this.newAccount);
		Assert.assertNotNull(this.savedAccount);
	}
	
}
