package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.uicontroller.OperationsUIController;
import com.fasterxml.jackson.core.JsonProcessingException;

public class OperationsUI {
		
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private OperationsUIController uiController;
	
	@Autowired
	private BufferedReader reader;
	
	private boolean responseRecived;
	private boolean accountClosed;
		
	private final String MENU_OPERATIONS = "\n1-.Make Deposit\n2-.Withdraw funds\n3-.Account's Balance\n4-.External Operation\n5-.Close Account\n6-.Exit\nOperation: ";
	private final String UNSUPPORTED_OPTION = "entry error, non existent option";

	private final String OPERATIONS_SECTION = "You can manipulate your account";
	private final int MAKE_DEPOSIT = 1;
	private final int WITHDRAWAL = 2;
	private final int GET_BALANCE = 3;
	private final int EXTERNAL_OP = 4;
	private final int CLOSE_ACCOUNT = 5;
	private final int EXIT = 6;
	private final String PROMPT_AMOUNT = "Amount: (required): ";
	private final String PROMPT_DESCRIPTION = "Description(optional): ";
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "Invalid entry, it's not a number\n";
	
	public void start(Account active) {
		int option = 0;
		
		do {
			try {
				accountClosed = false;
				responseRecived = false;
				System.out.println(OPERATIONS_SECTION);
				System.out.println(MENU_OPERATIONS);
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case MAKE_DEPOSIT:
						this.makeDeposit(active);
						break;
					case WITHDRAWAL:
						this.makeWithdrawal(active);
						break;
					case GET_BALANCE:
						this.getBalance(active);
						break;
					case EXTERNAL_OP:
						externalOperation(active);
						break;
					case CLOSE_ACCOUNT:
						closeAccount(active);
						break;
					case EXIT:
						responseRecived = true;
						break;
					default:
						System.out.println(UNSUPPORTED_OPTION);
						responseRecived = true;
				}
				while(!responseRecived) { System.out.println(); }
				if(accountClosed) {
					option = EXIT;
				}
			} catch(NumberFormatException e) {
				System.out.println(ENTRY_ERROR);
			} catch (IOException e) {
				System.err.println(READ_ERROR);
			} catch (MalformedRequestException e) {
				e.printStackTrace();
			}
		} while(option != EXIT);
	}
	
	private void closeAccount(Account active) throws UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {		
		uiController.setData(active.getId())
					.setOnSuccess((response, data) -> {
						  accountClosed = true;
						  System.out.println(response);
						  responseRecived = true;
				     })
					.setOnError(error -> {
						  System.out.println(error.getMessage());
						  accountClosed = false;
						  responseRecived = true;
				    })
					.deleteAccount();
	}
	
	private void externalOperation(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(createTransaction(reader, active, 2))
		.setOnSuccess((response, data) -> {
			  System.out.println(response);
			  responseRecived = true;
		 })
		 .setOnError(error -> {
			  System.out.println(error.getMessage());
			  responseRecived = true;
		 })
		 .externalOperation();
	}
	
	private void getBalance(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(active.getId())
					.setOnSuccess((response, data) -> {
						  System.out.println(response);
						  responseRecived = true;
				     })
					.setOnError(error -> {
						  System.out.println(error.getMessage());
						  responseRecived = true;
				    })
					.getAccountsBalance();
	}
	
	private void makeDeposit(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(createTransaction(reader, active, 2))
		.setOnSuccess((response, data) -> {
			  System.out.println(response);
			  responseRecived = true;
		 })
		 .setOnError(error -> {
			  System.out.println(error.getMessage());
			  responseRecived = true;
		 })
		 .makeDeposit();
	}
	
	private void makeWithdrawal(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(createTransaction(reader, active, 3))
		.setOnSuccess((response, data) -> {
			  System.out.println(response);
			  responseRecived = true;
	     })
		.setOnError(error -> {
			  System.out.println(error.getMessage());
			  responseRecived = true;
	    })
	    .makeWithdrawal();
	}

	private Transaction createTransaction(BufferedReader reader, Account activeAccount, int type) throws NumberFormatException, IOException {
		Transaction transaction;
		
		transaction = context.getBean(Transaction.class);
		System.out.println(PROMPT_AMOUNT);
		transaction.setAmount(Double.parseDouble(reader.readLine()));
		transaction.setAccount(activeAccount);
		System.out.println(PROMPT_DESCRIPTION);
		transaction.setDescription(reader.readLine());
		transaction.setType(type);
		transaction.setDate(Date.valueOf(LocalDate.now()));
		return transaction;
	}
	

}
