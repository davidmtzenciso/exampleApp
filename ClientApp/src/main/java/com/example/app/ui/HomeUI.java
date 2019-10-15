package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

public class HomeUI {
	
	@Autowired
	private BufferedReader reader;
	
	private final int OPEN_ACCOUNT = 1;
	private final int CLOSE_ACCOUNT = 2;
	private final int MAKE_DEPOSIT = 3;
	private final int WITHDRAWAL = 4;
	private final int EXIT = 5;
	private final String MENU_OPERATIONS = "\n1-.Open Account\n2-.Close Account\n3-.Make Deposit\n4-.Withdrawal\n4-.Account Balance\n5-.Exit";
	private final String MENU_OPEN_ACCOUNT = "";
	public void start() {
		int option = 0;
		
		do {
			try {
				System.out.println(MENU_OPERATIONS);
				option = Integer.parseInt(reader.readLine());
				switch(option) {
					case OPEN_ACCOUNT: openAccount(); break;
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
				System.err.println("invalid input, is not a number");
			} catch (IOException e) {
				System.err.println("input error, please try again");
			}
			
		} while(option != 5);
	}
	
	public void openAccount() {
		
	}
}
