package com.exercise.app.conf;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.exercise.app.ui.HomeUI;
import com.exercise.app.ui.LogInUI;
import com.exercise.clientApp.controller.LogInUIController;

@Configuration
public class AppConfig {
	
	@Bean
	public LogInUI logInUI() {
		return new LogInUI();
	}
	
	@Bean
	public HomeUI homeUI() {
		return new HomeUI();
	}
	
	@Bean
	public LogInUIController logInUIController() {
		return new LogInUIController();
	}
	
	@Bean
	@Scope("prototype")
	public BufferedReader bufferedReader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}
}
