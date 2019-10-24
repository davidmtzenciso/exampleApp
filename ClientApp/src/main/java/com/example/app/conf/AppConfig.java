package com.example.app.conf;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.app.ui.HomeUI;
import com.example.app.ui.LoginUI;
import com.example.app.ui.OperationsUI;
import com.example.app.uicontroller.HomeUIController;
import com.example.app.uicontroller.LoginUIController;
import com.example.app.uicontroller.OperationsUIController;
import com.example.app.uicontrollerimpl.HomeUIControllerImpl;
import com.example.app.uicontrollerimpl.LoginUIControllerImpl;
import com.example.app.uicontrollerimpl.OperationsUIControllerImpl;
import com.example.app.util.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
	
	@Bean
	public LoginUI logInUI() {
		return new LoginUI();
	}
	
	@Bean
	public HomeUI homeUI() {
		return new HomeUI();
	}
	
	@Bean
	public OperationsUI operationsUI() {
		return new OperationsUI();
	}
	
	@Bean
	public HomeUIController homeUIController() {
		return new HomeUIControllerImpl();
	}
	
	@Bean
	public LoginUIController logInUIController() {
		return new LoginUIControllerImpl();
	}
	
	@Bean
	public OperationsUIController operationsUIController() {
		return new OperationsUIControllerImpl();
	}
	
	@Bean
	public URLBuilder urlBuilder() {
		return new URLBuilder();
	}
	
	@Bean
	public BufferedReader bufferedReader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public AnnotationConfigApplicationContext context() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(ModelAppConfig.class);
		context.refresh();
		return context;
	}
}
