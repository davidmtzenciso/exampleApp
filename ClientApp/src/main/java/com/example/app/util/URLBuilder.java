package com.example.app.util;

public class URLBuilder {

	private final String HOST = "https://localhost:8080";
	private final String API_VERSION = "/1.0.0";
	private final String URI_ACCOUNT_MODULE = "/account";
	private final String URL_AUTH ="/auth";
	
	public String postAccount() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE;
	}
	
	public String postAuth() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URL_AUTH;
	}
	
}
