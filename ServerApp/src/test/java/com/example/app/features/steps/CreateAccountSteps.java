package com.example.app.features.steps;

import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.app.model.Account;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateAccountSteps implements En {
	
	@Autowired
	private Account newAccount;
	
	private Account savedAccount;
	
	public CreateAccountSteps() {
		
		Given("user wants to create an account with the properties", (DataTable accountDt) -> {
			List<Account> accounts = accountDt.asList(Account.class);
			this.newAccount = accounts.get(0);
		});
		
		When("user wants to save the new account {string}", (String testContext) -> {
			
		});
		
		Then("the operation {string}", (String expectedResult) -> {
			
		});
		
	}
}
