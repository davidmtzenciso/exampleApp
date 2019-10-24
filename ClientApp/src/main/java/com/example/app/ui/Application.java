package com.example.app.ui;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.app.conf.AppConfig;
import com.example.app.conf.ModelAppConfig;

public class Application {

	public static void main(String[] args) throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		HomeUI homeUI;
		
		context.register(AppConfig.class);
		context.register(ModelAppConfig.class);
		context.refresh();
		
		homeUI = context.getBean(HomeUI.class);
		homeUI.start();
		context.close();
	}
}
