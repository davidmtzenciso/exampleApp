package com.example.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Account implements Serializable {

	private Long id;
	private String firstName;
	private String lastName;
	private Integer pin;
	private String accountHoldersId;
	private Double balance;
	
	@JsonInclude(Include.NON_EMPTY)
	@JsonSerialize(as=ArrayList.class)
	@JsonDeserialize(as=ArrayList.class)
	private List<Transaction> transactions;
	
	@JsonIgnore
	private static final long serialVersionUID = -2209620568224742038L;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getAccountHoldersId() {
		return accountHoldersId;
	}
	public void setAccountHoldersId(String id) {
		this.accountHoldersId = id;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
}
