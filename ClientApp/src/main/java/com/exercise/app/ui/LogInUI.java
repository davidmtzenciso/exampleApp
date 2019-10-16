package com.exercise.app.ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.exercise.app.model.Credentials;
import com.exercise.app.ui.controller.LogInUIController;


public class LogInUI {

	@Autowired
	private HomeUI homeUI;
	
	@Autowired
	private BufferedReader reader;
	
	@Autowired
	private LogInUIController controller;
	 
	private final String ERROR_OPTION = "invalid option! please try again";
	private final String PROMPT_LOG_IN = "\n1-.Log In\n2-.Exit\n opcion: ";
	private final String PROMPT_ACCOUNT_NUM = "Account Number: ";
	private final String PROMPT_PIN = "PIN: ";
	
	public void authenticateUser()  {
		Long accountNum;
		Integer pin;
		Integer option = 0;
		
		do {
			try {
				System.out.println(PROMPT_LOG_IN);
				option = Integer.parseInt(reader.readLine());
				if(option == 1) {
					System.out.println(PROMPT_ACCOUNT_NUM);
					accountNum = Long.parseLong(reader.readLine());
					System.out.println(PROMPT_PIN);
					pin = Integer.parseInt(reader.readLine());
					System.out.println(controller.authenticate(new Credentials(accountNum, pin)));
					homeUI.start();
				} else if(option == 2) {
					System.exit(0);
				} else {
					System.out.println(ERROR_OPTION);
				}
			} catch(IOException e) {
				System.err.println("Input Error, please try again");
			} catch(NumberFormatException e) {
				System.err.println("invalid entry, it's not a number\n");
			}
		} while(option != 2);
	}
}
