package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;

@RestController
@RequestMapping(value = "/1.0.0/account")
public class AccountController {
	
	
	private Long idCounter;
	
	public AccountController() {
		this.idCounter = new Long(1000000);
	}

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(method = RequestMethod.POST, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account createAccount(@RequestBody Account account) {
		return this.accountService.save(account);
	}
	
	@RequestMapping(value="?id", method = RequestMethod.DELETE, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String deleteAccount(@RequestParam Long id) {
		try {
			return this.accountService.deleteAccount(id);
		}catch(OverdrawnAccountException e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST,	
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account authenticate(@RequestBody Account accountCredentials) {
		return this.accountService.getAccountbyIdNPin(accountCredentials.getId(), accountCredentials.getPin());
	}
	
	@RequestMapping(value = "/deposit", method = RequestMethod.POST,	
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account makeDeposit(@RequestBody Transaction operation) {
		try {
			return this.accountService.save(operation);
		} catch(InsuficientFundsException e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/withdrawal", method = RequestMethod.POST,	
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account makeWithdrawal(@RequestBody Transaction operation) {
		try {
			return this.accountService.save(operation);
		} catch(InsuficientFundsException e) {
			return null;
		}
	}
	
	@RequestMapping(value="/balance", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Double getBalance(@RequestParam Long id) {
		return accountService.getBalance(id);
	}
	
	@RequestMapping(value = "/debits-check", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Long processDebitsNChecks(@RequestParam Double amount, 
												@RequestParam Integer type, 
												@RequestParam String description) {
		return idCounter++;
	}
}
