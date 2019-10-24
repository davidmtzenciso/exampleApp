package com.example.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.app.exceptions.AuthenticationFailedException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.uicontroller.LoginUIController;

public class LoginUI extends Thread {
	
	@Autowired
	private LoginUIController uiController;
	
	@Autowired
	private BufferedReader reader;
	
	@Autowired
	private AnnotationConfigApplicationContext context;
		 
	private final String LOGIN_SECTION = "Authenticating user";
	private final String PROMPT_ACCOUNT_NUM = "\nAccount Number: ";
	private final String PROMPT_PIN = "\nPIN: ";
	private final String READ_ERROR = "Input Error, please try again";
	private final String ENTRY_ERROR = "invalid entry, it's not a number\n";
	private int attempts;
	private boolean responseRecived;
		
	public Account authenticate()  throws AuthenticationFailedException, MalformedRequestException, IOException, InterruptedException {
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
							.setOnSuccess( response -> {
								responseRecived = true;
								System.out.println(response);
								attempts = 4;
							})
							.setOnError(error -> {
								responseRecived = true;
								System.out.println(error);
								attempts++;
							})
							.authenticate();
				System.out.println("authenticating");
			} catch(IOException e) {
				System.err.println(READ_ERROR);
				attempts++;
			} catch(NumberFormatException e) {
				System.err.println(ENTRY_ERROR);
				attempts++;
			} 
			
			System.out.println("authenticating");
			while(!responseRecived) { System.out.print("."); }
		} while(attempts < 3);
		
		return uiController.getActiveAccount();
	}
}
