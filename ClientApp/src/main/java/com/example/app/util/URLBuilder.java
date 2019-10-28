package com.example.app.util;

public class URLBuilder {

	private final String HOST = "http://localhost:8080";
	private final String API_VERSION = "/1.0.0";
	private final String URI_ACCOUNT_MODULE = "/account";
	private final String URI_AUTH ="/auth";
	private final String URI_BALANCE = "/balance";
	private final String PARAM_ID = "?id=";
	private final String URI_DEPOSIT = "/deposit";
	private final String URL_WITHDRAWAL = "/withdrawal";
	private final String URI_DEBITS_CHECKS = "/external/debits-checks";
	
	public String postAccount() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE;
	}
	
	public String deleteAccount(Long id) {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + PARAM_ID + id;
	}
	
	public String postCredentials() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URI_AUTH;
	}
	
	public String getBalance(Long id) {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URI_BALANCE + PARAM_ID + id;
	}
	
	public String postDeposit() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URI_DEPOSIT;
	}
	
	public String postWithdrawal() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URL_WITHDRAWAL;
	}
	
	public String postExternalDebitsNChecks() {
		return HOST + API_VERSION + URI_ACCOUNT_MODULE + URI_DEBITS_CHECKS;
	}
}
