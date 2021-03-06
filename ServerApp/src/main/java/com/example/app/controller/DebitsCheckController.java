package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.exception.OverdrawnAccountException;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.exception.InsufficientFundsException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;
import com.example.app.service.TransactionService;

@RestController
@RequestMapping(value = "/1.0.0/debits-check/account")
public class DebitsCheckController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(method = RequestMethod.POST, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("#oauth2.hasAnyScope('write')")
	public @ResponseBody Account createAccount(@RequestBody Account account) throws FailedEntityValidationException {
		return this.accountService.save(account);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void deleteAccount(@RequestParam Long id) throws OverdrawnAccountException, AccountNotFoundException {
		 this.accountService.deleteAccount(id);
	}
	
	@RequestMapping(value="/balance", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Double getBalance(@RequestParam Long id) throws AccountNotFoundException {
		return this.accountService.getBalance(id);
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST,	
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Account authenticate(@RequestBody Credentials credentials) throws AccountNotFoundException {
		return this.accountService.getAccountByIdNPin(credentials.getAccountNumber(), credentials.getPin());
	}
	
	@RequestMapping(value = "/deposit", method = RequestMethod.POST,	
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Transaction makeDeposit(@RequestBody Transaction operation) throws FailedEntityValidationException, AccountNotFoundException {
		this.accountService.addAmount(operation.getAmount(), operation.getAccount().getId());
		return this.transactionService.save(operation);
	}
	
	@RequestMapping(value = "/withdrawal", method = RequestMethod.POST,	
		consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Transaction makeWithdrawal(@RequestBody Transaction operation) throws InsufficientFundsException, FailedEntityValidationException, AccountNotFoundException {
		this.accountService.extractAmount(operation.getAmount(), operation.getAccount().getId());
		return this.transactionService.save(operation);
	}
	
	@RequestMapping(value = "/external", method = RequestMethod.POST,
	consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
	produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Long processDebitsNChecks(@RequestBody Transaction operation) throws FailedEntityValidationException {
		
		if(operation.getAmount() == null || operation.getType() == null || operation.getDescription() == null) {
			throw new FailedEntityValidationException("unable to process, missing data");
		}
		return null;
	}
}
