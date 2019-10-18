package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.exception.InsuficientFundsException;
import com.example.app.exception.OverdrawnAccountException;
import com.example.app.exception.AccountNotFoundException;
import com.example.app.exception.FailedEntityValidationException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.service.AccountService;

@RestController
@RequestMapping(value = "/1.0.0/account")
public class AccountController {
	
	private Long idCounter;
	private final String SUCCESS = "Successful operation, id ";
	
	public AccountController() {
		this.idCounter = new Long(1000000);
	}

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(method = RequestMethod.POST, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody Account account) {
		try {
			return new ResponseEntity<>(this.accountService.save(account), HttpStatus.OK);
		} catch(FailedEntityValidationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, 
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> deleteAccount(@RequestParam Long id) {
		try {
			
			return new ResponseEntity<>(this.accountService.deleteAccount(id), HttpStatus.OK);
		} catch(AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch(OverdrawnAccountException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST,	
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public  ResponseEntity<Object> authenticate(@RequestBody Account credentials) {
		try {
			Account account = this.accountService.getAccountbyIdNPin(credentials.getId(), credentials.getPin());
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@RequestMapping(value = "/deposit", method = RequestMethod.POST,	
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> makeDeposit(@RequestBody Transaction operation) {
		try {
			Transaction saved = this.accountService.makeDeposit(operation);
			return new ResponseEntity<>(SUCCESS + saved.getId(), HttpStatus.OK);
		} catch(AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@RequestMapping(value = "/withdrawal", method = RequestMethod.POST,	
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Transaction makeWithdrawal(@RequestBody Transaction operation) {
		try {
			return this.accountService.makeWithdrawal(operation);
		} catch(InsuficientFundsException | AccountNotFoundException e) {
			return null;
		}
	}
	
	@RequestMapping(value="/balance", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String getBalance(@RequestParam Long id) {
		try {
			return String.valueOf(accountService.getBalance(id));
		} catch (AccountNotFoundException e) {
			return e.getMessage();
		}
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
