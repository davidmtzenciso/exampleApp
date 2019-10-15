package exercise.com.app.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exercise.com.app.controller.DebitsCheckController;
import exercise.com.app.model.Account;
import exercise.com.app.model.Transaction;
import exercise.com.app.service.AccountService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Account.class, Transaction.class})
@WebMvcTest(DebitsCheckController.class)
public class DebitsCheckControllerTest {

	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockBean
	 private AccountService accountService;
	 
	 @Autowired
	 private ObjectMapper mapper;
	 
	 @Autowired
	 private Account newAccount;
	 
	 
	 private final String URL_POST_ACCOUNT = "/debits-check/account";
	 private final String URI_POST_ACCOUNT_AUTH = "/debits-check/account/auth";

	 @Test
	 public void testCorrectPost() throws JsonProcessingException, Exception  {
		 newAccount.setId(new Long(1));
		 when(accountService.createAccount(this.newAccount)).thenReturn(newAccount);
		 
		 mockMvc.perform(post(URL_POST_ACCOUNT)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .accept(MediaType.APPLICATION_JSON_UTF8)
		 				.content(mapper.writeValueAsString(this.newAccount)))
				        .andExpect(status().isOk())
		 				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		 				.andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(this.newAccount)));
	 }
	 
	 @Test
	 public void testGetAccountByIdNPinWrongRequestBody() throws JsonProcessingException, Exception {
		 mockMvc.perform(post(URI_POST_ACCOUNT_AUTH)
				 		.contentType(MediaType.APPLICATION_JSON)
				 		.accept(MediaType.APPLICATION_JSON)
				 		.content(mapper.writeValueAsString(this.newAccount)))
				 		.andExpect(status().isBadRequest());
	 }
}
