package com.example.app.integration.cucumber;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.app.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateAccountSteps implements En {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Account newAccount;
		
	private final String URI_POST_ACCOUNT= "/1.0.0/debits-check/account";
	
	public CreateAccountSteps() {
		
		Given("user wants to create an account with the properties", (DataTable accountDt) -> {
			List<Account> accounts = accountDt.asList(Account.class);
			this.newAccount = accounts.get(0);
		});
		
		When("user wants to save the new account {string}", (String testContext) -> {
			
		});
		
		Then("the operation {string}", (String expectedResult) -> {
			mvc.perform(post(URI_POST_ACCOUNT)
				      .contentType(MediaType.APPLICATION_JSON)
				      .content((mapper.writeValueAsString(this.newAccount))))
				      .andExpect(status().isOk())
				      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		});
		
	}
}
