package exercise.com.app.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exercise.com.app.conf.InitAccount;
import exercise.com.app.controller.DebitsCheckController;
import exercise.com.app.model.Account;
import exercise.com.app.service.AccountService;

@RunWith(SpringRunner.class)
@WebMvcTest(DebitsCheckController.class)
public class DebitsCheckControllerTest implements InitAccount {

	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockBean
	 private AccountService accountService;
	 
	 @Autowired
	 private ObjectMapper mapper;
	 
	 private Account newAccount;

	 @Before
	 public void init() {
		 this.newAccount = new Account();
		 this.newAccount = this.initialize(this.newAccount);
	 }
	 
	 @Test
	 public void testCorrectPost() throws JsonProcessingException, Exception  {
		 
		 when(accountService.createAccount(this.newAccount)).thenReturn(new Account());
		 
		 mockMvc.perform(post("/debits-check/account")
				        .contentType("application/json")
		 				.content(mapper.writeValueAsString(this.newAccount)))
				        .andExpect(status().isOk());
	 }
}
