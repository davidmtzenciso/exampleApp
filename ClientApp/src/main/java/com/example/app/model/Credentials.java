package com.example.app.model;

public class Credentials {

	private Long accountNumber;
	private Integer pin;
	
	public Credentials(Long accountNumber, Integer pin) {
		super();
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
