package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.model.Credentials;
import com.example.app.uicontrollerimpl.LogInUIController;


public class LogInUI {
	
	@Autowired
	private BufferedReader reader;
	
	@Autowired
	private LogInUIController controller;
	 
	private final String LOGIN_SECTION = "Authenticating user";
	private final String PROMPT_ACCOUNT_NUM = "\nAccount Number: ";
	private final String PROMPT_PIN = "\nPIN: ";
	
	public void authenticateUser()  {
		Long accountNum;
		Integer pin;
		Integer option = 0;
		
		do {
			try {
				System.out.println(LOGIN_SECTION);
				System.out.println(PROMPT_ACCOUNT_NUM);
				accountNum = Long.parseLong(reader.readLine());
				System.out.println(PROMPT_PIN);
				pin = Integer.parseInt(reader.readLine());
				
				
			} catch(IOException e) {
				System.err.println("Input Error, please try again");
			} catch(NumberFormatException e) {
				System.err.println("invalid entry, it's not a number\n");
			}
		} while(option != 2);
	}
}
