package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.model.Account;
import com.example.app.service.AccountService;

@RestController
@RequestMapping(value = "/1.0.0/debits-check")
public class DebitsCheckController {

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/account", method = RequestMethod.POST, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account createAccount(@RequestBody Account account) {
		return this.accountService.createAccount(account);
	}
	
	@RequestMapping(value = "/account/auth", method = RequestMethod.POST,	
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account getAccountbyIdNPin(@RequestBody Account accountCredentials) {
		return this.accountService.getAccountbyIdNPin(accountCredentials.getId(), accountCredentials.getPin());
	}
	
}
