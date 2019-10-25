package com.example.app.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.app.exceptions.AttemptsExceededException;
import com.example.app.exceptions.MalformedRequestException;
import com.example.app.model.Account;
import com.example.app.model.Credentials;
import com.example.app.uicontroller.LoginUIController;
import com.example.app.util.AbstractUI;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LoginUI extends AbstractUI {
	
	@Autowired
	private LoginUIController uiController;
	
	private Account active;
	private int attempts;
		 
	private final String LOGIN_SECTION = "Authenticating user";
	private final String PROMPT_ACCOUNT_NUM = "\nAccount Number: ";
	private final String PROMPT_PIN = "\nPIN: ";
	private final String ERROR_ATTEMPTS_EXCEEDED = "attempts exceeded(3), operation canceled";
		
	public Account start()  throws MalformedRequestException, IOException, AttemptsExceededException {
		Credentials credentials = context.getBean(Credentials.class);
	    attempts = 0;
		
		do {
			try {
				System.out.println(LOGIN_SECTION);
				System.out.println(PROMPT_ACCOUNT_NUM);
				credentials.setAccountNumber(Long.parseLong(reader.readLine()));
				System.out.println(PROMPT_PIN);
				credentials.setPin(Integer.parseInt(reader.readLine()));
				authenticate(credentials);
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
	
	private void authenticate(Credentials credentials) throws UnsupportedEncodingException, JsonProcessingException, MalformedRequestException, IOException {
		uiController.setData(credentials)
		.setOnSuccess( (response, data) -> {
			attempts = 5;
			this.active = data;
			System.out.println(response);

		})
		.setOnError(error -> {
			System.out.println(error.getMessage());
			attempts++;
		})
		.setOnProgress(progress -> this.progress = progress)
		.authenticate();
		
		this.waitingResponse();
	}

}
