package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.uicontroller.OperationsUIController;
import com.example.app.util.AbstractUI;
import com.fasterxml.jackson.core.JsonProcessingException;

public class OperationsUI  extends AbstractUI {
		
	@Autowired
	private OperationsUIController uiController;
	
	private boolean accountClosed;
		
	private final String MENU_OPERATIONS = "\n1-.Make Deposit\n2-.Withdraw funds\n3-.Account's Balance\n4-.External Operation\n5-.Close Account\n6-.Exit\nOperation: ";
	private final String OPERATIONS_SECTION = "You can manipulate your account";
	private final String PROMPT_AMOUNT = "Amount: (required): ";
	private final String PROMPT_DESCRIPTION = "Description (optional): ";
	private final int MAKE_DEPOSIT = 1;
	private final int WITHDRAWAL = 2;
	private final int GET_BALANCE = 3;
	private final int EXTERNAL_OP = 4;
	private final int CLOSE_ACCOUNT = 5;
	private final int EXIT = 6;
	
	public void start(Account active) {
		int option = 0;
		
		do {
			try {
				accountClosed = false;
				System.out.println(OPERATIONS_SECTION);
				System.out.println(MENU_OPERATIONS);
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case MAKE_DEPOSIT: this.makeDeposit(active);
						break;
					case WITHDRAWAL: this.makeWithdrawal(active);
						break;
					case GET_BALANCE: this.getBalance(active);
						break;
					case EXTERNAL_OP: this.externalOperation(active);
						break;
					case CLOSE_ACCOUNT: this.closeAccount(active);
						break;
					case EXIT:
						break;
					default:
						System.out.println(UNSUPPORTED_OPTION);
				}
				if(accountClosed) {
					option = EXIT;
				}
			} catch(NumberFormatException e) {
				System.out.println(ENTRY_ERROR);
			} catch (IOException e) {
				System.err.println(READ_ERROR);
			} catch (MalformedRequestException e) {
				System.err.println(e.getMessage());
			}
		} while(option != EXIT);
	}
	
	private void closeAccount(Account active) throws UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {		
		uiController.setData(active.getId())
					.setOnSuccess((response, data) -> {
						  accountClosed = true;
						  System.out.println(response);
				     })
					.setOnError(error -> {
						  System.out.println(error.getMessage());
						  accountClosed = false;
				    })
					.setOnProgress(progress -> this.progress = progress)
					.deleteAccount();
		System.out.print("closing account");
		this.waitingResponse();
	}
	
	private void externalOperation(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(createTransaction(reader, active, 2))
					 .setOnSuccess((response, data) -> {
						  System.out.println(response);
					 })
					 .setOnError(error -> {
						  System.out.println(error.getMessage());
					 })
					 .setOnProgress(progress -> this.progress = progress)
					 .externalOperation();
		System.out.print("processing operation");
		this.waitingResponse();
	}
	
	private void getBalance(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(active.getId())
					.setOnSuccess((response, data) -> {
						  System.out.println(response);
				     })
					.setOnError(error -> {
						  System.out.println(error.getMessage());
				    })
					.setOnProgress(progress -> this.progress = progress)
					.getAccountsBalance();
		System.out.print("getting balance");
		this.waitingResponse();
	}
	
	private void makeDeposit(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(createTransaction(reader, active, 2))
					.setOnSuccess((response, data) -> {
						  System.out.println(response);
					 })
					.setOnError(error -> {
						  System.out.println(error.getMessage());
					 })
					.setOnProgress(progress -> this.progress = progress)
					.makeDeposit();
		System.out.print("making deposit");
		this.waitingResponse();
	}
	
	private void makeWithdrawal(Account active) throws NumberFormatException, UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(createTransaction(reader, active, 3))
					.setOnSuccess((response, data) -> {
						  System.out.println(response);
				     })
					.setOnError(error -> {
						  System.out.println(error.getMessage());
				    })
					.setOnProgress(progress -> this.progress = progress)
				    .makeWithdrawal();
		System.out.print("withdrawing funds");
		this.waitingResponse();
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
