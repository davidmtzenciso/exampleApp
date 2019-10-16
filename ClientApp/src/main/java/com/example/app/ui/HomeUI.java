package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.AttemptsExceededException;
import com.example.app.model.Account;

public class HomeUI {
	
	@Autowired
	private BufferedReader reader;
	
	private final int OPEN_ACCOUNT = 1;
	private final int CLOSE_ACCOUNT = 2;
	private final int MAKE_DEPOSIT = 3;
	private final int WITHDRAWAL = 4;
	private final int EXIT = 5;
	private final String MENU_OPERATIONS = "\n1-.Open Account\n2-.Close Account\n3-.Make Deposit\n4-.Withdrawal\n4-.Account Balance\n5-.Exit";
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
	private final String PROMPT_ACCOUT_HOLDERS_ID = "\nID: ";
	private final String PROMPT_BALANCE = "\nInitial balance: ";
	
	public void start() {
		Account account;
		
		int option = 0;
		
		do {
			try {
				System.out.println(MENU_OPERATIONS);
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case OPEN_ACCOUNT: 
						account = createAccount(this.reader);
						
					case CLOSE_ACCOUNT:
						break;
					case MAKE_DEPOSIT:
						break;
					case WITHDRAWAL:
						break;
					case EXIT:
						break;
				}
			} catch(NumberFormatException e) {
				System.err.println("invalid option, is not a number");
			} 
			catch(AttemptsExceededException e) {
				System.err.println(e.getMessage());
			}
			catch (IOException e) {
				System.err.println("input error, please try again");
			}
		} while(option != 5);
	}
	
	private Account createAccount(BufferedReader reader) throws IOException, AttemptsExceededException {
		Account account = new Account();
		String balance;
		
		try {
			System.out.println(OPEN_ACCOUNT_SEC);
			account = readFirstNLastName(account, reader, 0);
			account.setPin(readAccountPIN(reader, 0));
			System.out.println(PROMPT_ACCOUT_HOLDERS_ID);
			account.setAccountHoldersName(reader.readLine());
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
				System.out.println(PROMPT_LAST_NAME);
				lastName = reader.readLine();
				if(lastName.isEmpty()) {
					System.err.println(ERROR_LAST_NAME_REQUIRED);
					return readFirstNLastName(account, reader, attempts + 1);
				} else {
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
