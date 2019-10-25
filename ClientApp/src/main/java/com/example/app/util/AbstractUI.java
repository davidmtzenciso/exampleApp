package com.example.app.util;

import java.io.BufferedReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class AbstractUI {

	@Autowired
	protected BufferedReader reader;
	
	@Autowired
	protected AnnotationConfigApplicationContext context;
	
	protected int progress;
	
	protected final String UNSUPPORTED_OPTION = "entry error, non existent option";
	protected final String READ_ERROR = "Input Error, please try again";
	protected final String ENTRY_ERROR = "Invalid entry, it's not a number\n";
	
	protected void waitingResponse() {
		while(this.progress < 100) { 
			int i=0; 
			if(i %3 == 0) { 
				System.out.print("."); 
			} i++; 
		}
		System.out.println();
	}
}
