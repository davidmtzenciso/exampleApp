package com.example.app.conf;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.example.app.ui.HomeUI;
import com.example.app.ui.LogInUI;
import com.example.app.uicontrollerimpl.LogInUIController;
import com.example.app.util.URLBuilder;

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
	public URLBuilder urlBuilder() {
		return new URLBuilder();
	}
	
	@Bean
	@Scope("prototype")
	public BufferedReader bufferedReader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}
}