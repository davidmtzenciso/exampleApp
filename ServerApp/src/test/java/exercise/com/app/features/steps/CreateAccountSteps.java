package exercise.com.app.features.steps;


import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import exercise.com.app.model.Account;
import exercise.com.app.repository.AccountRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateAccountSteps implements En {
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private Account newAccount;
	
	private Account savedAccount;
	
	public CreateAccountSteps() {
		
		Given("user wants to create an account with the properties", (DataTable accountDt) -> {
			List<Account> accounts = accountDt.asList(Account.class);
			this.newAccount = accounts.get(0);
		});
		
		When("user wants to save the new account {string}", (String testContext) -> {
			this.savedAccount = this.repository.save(newAccount);
		});
		
		Then("the operation {string}", (String expectedResult) -> {
			Assert.assertNotNull(this.savedAccount);
		});
		
	}
}
