package com.example.app.ui;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.app.conf.AppConfig;

public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		HomeUI homeUI;
		
		context.register(AppConfig.class);
		context.refresh();
		
		homeUI = context.getBean(HomeUI.class);
		homeUI.start();
		context.close();
	}
}
