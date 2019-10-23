package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.model.Account;

public class OperationsUI {
	
	@Autowired
	private BufferedReader reader;
	
	private final String MENU_OPERATIONS = "\n1-.Make Deposit\n2-.Withdraw funds\n3-.Account's Balance\n4-.Close Account\n5-.Exit";
	private final String OPERATIONS_SECTION = "Usted esta en operaciones";
	private final int MAKE_DEPOSIT = 1;
	private final int WITHDRAWAL = 2;
	private final int GET_BALANCE = 3;
	private final int CLOSE_ACCOUNT = 4;
	private final int EXIT = 5;
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "invalid entry, it's not a number\n";
	
	public void start(Account account) {
		int option = 0;
		
		do {
			System.out.println(OPERATIONS_SECTION);
			System.out.println(MENU_OPERATIONS);
			try {
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case MAKE_DEPOSIT:
						break;
					case WITHDRAWAL:
						break;
					case GET_BALANCE:
						break;
					case CLOSE_ACCOUNT:
						break;
					case EXIT:
						break;
					default:
				}
			} catch(NumberFormatException e) {
				System.out.println(ENTRY_ERROR);
			} catch (IOException e) {
				System.err.println(READ_ERROR);
			}
		} while(option != EXIT);
	}

}
