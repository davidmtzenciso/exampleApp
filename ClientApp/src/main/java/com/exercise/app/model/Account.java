package com.exercise.app.model;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable {

	private Long accountNumber;
	private String firstName;
	private String lastName;
	private Integer pin;
	private String accountHoldersName;
	private Double balance;
	private Credentials credentials;
	private List<Transaction> transactions;
	private static final long serialVersionUID = -2209620568224742038L;
	
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	public String getAccountHoldersName() {
		return accountHoldersName;
	}
	public void setAccountHoldersName(String accountHoldersName) {
		this.accountHoldersName = accountHoldersName;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
}
