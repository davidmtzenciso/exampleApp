package com.example.app.util;

public class URLBuilder {

	private final String HOST = "http://localhost:8080";
	private final String API_VERSION = "/1.0.0";
	private final String URI_ACCOUNT_MODULE = "/account";
	private final String URI_AUTH ="/auth";
	private final String URI_BALANCE = "/balance";
	private final String PARAM_ID = "?id=";
	private final String DEPOSIT = "/deposit";
	private final String WITHDRAWAL = "/withdrawal";
	private final String DEBITS_CHECKS = "/external/debits-checks";
	
	public String createAccount() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE;
	}
	
	public String deleteAccount(Long id) {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + PARAM_ID + id;
	}
	
	public String authentication() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URI_AUTH;
	}
	
	public String getBalance(Long id) {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URI_BALANCE + PARAM_ID + id;
	}
	
	public String makeDeposit() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + DEPOSIT;
	}
	
	public String makeWithdrawal() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + WITHDRAWAL;
	}
	
	public String externalDebitsNChecks() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + DEBITS_CHECKS;
	}
}
