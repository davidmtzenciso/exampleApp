package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.example.app.exceptions.AuthenticationFailedException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.uicontroller.LoginUIController;

public class LoginUI {
	
	@Autowired
	private BufferedReader reader;
	
	@Autowired
	private LoginUIController uiController;
	
	@Autowired
	ApplicationContext context;
	
	private Credentials credentials;
	 
	private final String LOGIN_SECTION = "Authenticating user";
	private final String PROMPT_ACCOUNT_NUM = "\nAccount Number: ";
	private final String PROMPT_PIN = "\nPIN: ";
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "invalid entry, it's not a number\n";
	private int attempts;
	
	public Account authenticate()  throws AuthenticationFailedException, MalformedRequestException{
		Long accountNumber;
		Integer pin;
	    attempts = 0;
		
		do {
			try {
				System.out.println(LOGIN_SECTION);
				System.out.println(PROMPT_ACCOUNT_NUM);
				accountNumber = Long.parseLong(reader.readLine());
				System.out.println(PROMPT_PIN);
				pin = Integer.parseInt(reader.readLine());
				credentials = context.getBean(Credentials.class);
				credentials.setAccountNumber(accountNumber);
				credentials.setPin(pin);
				uiController.setData(credentials)
							.setOnSuccess( response -> {	
								System.out.println(response);
								attempts = 3;
							})
							.setOnError(error -> {
								System.out.println(error);
								attempts++;
							})
							.authenticate();
				
			} catch(IOException e) {
				System.err.println(READ_ERROR);
				attempts++;
			} catch(NumberFormatException e) {
				System.err.println(ENTRY_ERROR);
				attempts++;
			}
		} while(attempts < 3);
		return uiController.getActiveAccount();
	}
}
