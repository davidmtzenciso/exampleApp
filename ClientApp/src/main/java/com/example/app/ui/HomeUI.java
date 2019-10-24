package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.AttemptsExceededException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.uicontroller.HomeUIController;
import com.fasterxml.jackson.core.JsonProcessingException;

public class HomeUI  {
	
	@Autowired
	private LoginUI loginUI;
	
	@Autowired
	private OperationsUI operationsUI;
	
	@Autowired
	private BufferedReader reader;
	
	@Autowired
	private HomeUIController uiController;
		
	private boolean responseRecived;
	
	private final int OPEN_ACCOUNT = 1;
	private final int LOGIN = 2;
	private final int EXIT = 3;
	private final String HOME_MENU = "1-.Open account\n2-.Log in\n3-.Exit\nOption: ";
	private final String UNSUPPORTED_OPTION = "entry error, non existent option";
	private final String OPEN_ACCOUNT_SEC = "Open Account\n";
	private final String PROMPT_FIRST_NAME = "\nFirst name: ";
	private final String PROMPT_LAST_NAME = "\nLast name:";
	private final String PROMPT_PIN = "\nPIN: ";
	private final String PROMPT_PIN_CONFIRMATION = "Please enter PIN again: ";
	private final String ERROR_PIN_ZERO = "PIN cannot be 0, enter 4 digit number"; 
	private final String ERROR_PIN_CONFIRMATION = "PINs doesn't match, please try again";
	private final String ERROR_PIN_NON_NUMBER = "entry is not a number, PIN must be a number";
	private final String ERROR_FIRST_NAME_REQUIRED = "First name is required, entry cannot be empty";
	private final String ERROR_LAST_NAME_REQUIRED = "Last name is required, entry cannot be empty";
	private final String ERROR_ATTEMPTS_EXCEEDED = "attempts exceeded(3), operation canceled";
	private final String PROMPT_ACCOUT_HOLDERS_ID = "\nAccount's holder ID: ";
	private final String PROMPT_BALANCE = "\nInitial balance: ";
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "invalid entry, it's not a number\n";
	private final String EXIT_MSG = "System closed!";
	
	public void start() {
		int option = 0;
		
		do {
			try {
				responseRecived = false;
				System.out.println(HOME_MENU);
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case OPEN_ACCOUNT: 
						openAccount();
						break;
					case LOGIN:
						operationsUI.start(loginUI.authenticate());
						responseRecived = true;
						break;
					case EXIT:
						System.out.println(EXIT_MSG);
						responseRecived = true;
						break;
					default:
						System.out.println(UNSUPPORTED_OPTION);
						responseRecived = true;
				}
				while(!responseRecived) { System.out.println();}
			} catch(NumberFormatException e) {
				System.err.println(READ_ERROR);
			} 
			catch(AttemptsExceededException e) {
				System.err.println(e.getMessage());
			}
			catch (IOException e) {
				System.err.println(ENTRY_ERROR);
			} 
			catch(MalformedRequestException e) {
				System.err.println(e.getMessage());
			}
		} while(option != EXIT);
	}
	
	private void openAccount() throws UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException, AttemptsExceededException {
		uiController.setData(this.createAccount(reader))
		  .setOnSuccess((response, data) -> {
			  System.out.println(response + ", account number : " + data.getId() + ", pin: "+ data.getPin());
			  responseRecived = true;
		  })
		  .setOnError(error -> {
			  System.out.println(error.getMessage());
			  responseRecived = true;
		  })
		  .openAccount();
	}
	
	private Account createAccount(BufferedReader reader) throws IOException, AttemptsExceededException {
		Account account = new Account();
		String balance;
		
		try {
			System.out.println(OPEN_ACCOUNT_SEC);
			account = readFirstNLastName(account, reader, 0);
			account.setPin(readAccountPIN(reader, 0));
			System.out.println(PROMPT_ACCOUT_HOLDERS_ID);
			account.setAccountHoldersId(reader.readLine());
			System.out.println(PROMPT_BALANCE);
			balance = reader.readLine();
			account.setBalance(balance.isEmpty() ? 0.0 : Double.parseDouble(balance));
		} 
		catch (NumberFormatException e) {
			account.setBalance(0.0);
		}
		return account;
	}
	
	private Account readFirstNLastName(Account account, BufferedReader reader, int attempts) throws IOException, AttemptsExceededException {
		String firstName;
		String lastName;
		
		if(attempts < 3) {
			System.out.println(PROMPT_FIRST_NAME);
			firstName = reader.readLine();
			if(firstName.isEmpty()) {
				System.err.println(ERROR_FIRST_NAME_REQUIRED);
				return readFirstNLastName(account, reader, attempts + 1);
			} else { 
				account.setFirstName(firstName);
				System.out.println(PROMPT_LAST_NAME);
				lastName = reader.readLine();
				if(lastName.isEmpty()) {
					System.err.println(ERROR_LAST_NAME_REQUIRED);
					return readFirstNLastName(account, reader, attempts + 1);
				} else {
					account.setLastName(lastName);
					return account;
				}
			}
		} else {
			throw new AttemptsExceededException(ERROR_ATTEMPTS_EXCEEDED);
		}
	}
	
	private int readAccountPIN(BufferedReader reader, int attempts) throws IOException, AttemptsExceededException {		
		int pinConfirmation = 0;
		int pin = 0;
		
		if(attempts < 3) {
			try {
				System.out.println(PROMPT_PIN);
				pin = Integer.parseInt(reader.readLine());
				
				if(pin > 0) {
					System.out.println(PROMPT_PIN_CONFIRMATION);
					pinConfirmation = Integer.parseInt(reader.readLine());
					if(pin == pinConfirmation) {
						return pin;
					} else {
						System.err.println(ERROR_PIN_CONFIRMATION);
						return readAccountPIN(reader, attempts + 1);
					}
				} else {
					System.err.println(ERROR_PIN_ZERO);
					return readAccountPIN(reader, attempts + 1);
				}
			} catch(NumberFormatException e) {
				System.err.println(ERROR_PIN_NON_NUMBER);
				return readAccountPIN(reader, attempts + 1);
			}
		} else {
			throw new AttemptsExceededException(ERROR_ATTEMPTS_EXCEEDED);
		}
	}
		
}
