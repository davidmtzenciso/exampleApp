package com.example.app.model;

import java.io.Serializable;

public class Credentials implements Serializable  {

	private Long accountNumber;
	private Integer pin;
	
	private static final long serialVersionUID = 7031738121556269844L;
	
	public Credentials() {}
	
	public Credentials(Long accountNumber, Integer pin) {
		this.accountNumber = accountNumber;
		this.pin = pin;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	
	
}
