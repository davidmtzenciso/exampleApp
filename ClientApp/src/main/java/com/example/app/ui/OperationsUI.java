package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.example.app.uicontroller.OperationsUIController;

public class OperationsUI {
	
	@Autowired
	private BufferedReader reader;
		
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private OperationsUIController uiController;
		
	private final String MENU_OPERATIONS = "\n1-.Make Deposit\n2-.Withdraw funds\n3-.Account's Balance\n4-.Close Account\n5-.Exit";
	private final String OPERATIONS_SECTION = "Usted esta en operaciones";
	private final int MAKE_DEPOSIT = 1;
	private final int WITHDRAWAL = 2;
	private final int GET_BALANCE = 3;
	private final int CLOSE_ACCOUNT = 4;
	private final int EXIT = 5;
	private final String PROMPT_ACCOUNT_ID = "Please enter account number: ";
	private final String PROMPT_AMOUNT = "Amount: (required): ";
	private final String PROMPT_DESCRIPTION = "Description(optional): ";
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "Invalid entry, it's not a number\n";
	private final String EXIT_MSG = "System closed!";
	
	public void start(Account active) {
		int option = 0;
		
		do {
			System.out.println(OPERATIONS_SECTION);
			System.out.println(MENU_OPERATIONS);
			try {
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case MAKE_DEPOSIT:
						uiController.setData(createTransaction(reader, active, 2))
									.setOnSuccess(response -> System.out.println(response))
									.setOnError(error -> System.out.println(error))
									.makeDeposit();
						break;
					case WITHDRAWAL:
						uiController.setData(createTransaction(reader, active, 3))
									.setOnSuccess(response -> System.out.println(response))
									.setOnError(error -> System.out.println(error))
									.makeWithdrawal();
						break;
					case GET_BALANCE:
						System.out.println(PROMPT_ACCOUNT_ID);
						uiController.setData(Long.parseLong(reader.readLine()))
									.setOnSuccess(response -> System.out.println(response))
									.setOnError(error -> System.out.println(error))
									.getBalance();
					case CLOSE_ACCOUNT:
						System.out.println(PROMPT_ACCOUNT_ID);
						uiController.setData(Long.parseLong(reader.readLine()))
									.setOnSuccess(response -> System.out.println(response))
									.setOnError(error -> System.out.println(error))
									.deleteAccount();
						break;
					case EXIT:
						System.out.println(EXIT_MSG);
						break;
					default:
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
