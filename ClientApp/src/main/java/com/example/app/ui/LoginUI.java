package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.app.exceptions.AttemptsExceededException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.uicontroller.LoginUIController;

public class LoginUI {
	
	@Autowired
	private LoginUIController uiController;
	
	@Autowired
	private BufferedReader reader;
	
	private Account active;
	
	@Autowired
	private AnnotationConfigApplicationContext context;
		 
	private final String LOGIN_SECTION = "Authenticating user";
	private final String PROMPT_ACCOUNT_NUM = "\nAccount Number: ";
	private final String PROMPT_PIN = "\nPIN: ";
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "invalid entry, it's not a number\n";
	private final String ERROR_ATTEMPTS_EXCEEDED = "attempts exceeded(3), operation canceled";

	private boolean responseRecived;
	private int attempts;
		
	public Account authenticate()  throws MalformedRequestException, IOException, AttemptsExceededException {
		Credentials credentials = context.getBean(Credentials.class);
	    attempts = 0;
		
		do {
			try {
				responseRecived = false;
				System.out.println(LOGIN_SECTION);
				System.out.println(PROMPT_ACCOUNT_NUM);
				credentials.setAccountNumber(Long.parseLong(reader.readLine()));
				System.out.println(PROMPT_PIN);
				credentials.setPin(Integer.parseInt(reader.readLine()));
				uiController.setData(credentials)
							.setOnSuccess( (response, data) -> {
								attempts = 5;
								this.active = data;
								System.out.println(response);
								responseRecived = true;

							})
							.setOnError(error -> {
								System.out.println(error.getMessage());
								attempts++;
								responseRecived = true;

							})
							.authenticate();
				while(!responseRecived) { System.out.println();}
			} catch(IOException e) {
				System.err.println(READ_ERROR);
				attempts++;
			} catch(NumberFormatException e) {
				System.err.println(ENTRY_ERROR);
				attempts++;
			} 
		} while(attempts < 3);
		if(attempts == 3) {
			throw new AttemptsExceededException(ERROR_ATTEMPTS_EXCEEDED);
		} else {
			return active;
		}
	}
}
