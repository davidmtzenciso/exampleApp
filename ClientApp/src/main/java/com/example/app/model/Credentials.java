package com.example.app.model;

import java.io.Serializable;

public class Credentials implements Serializable {

	private static final long serialVersionUID = -2468920800107022005L;
	private Long accountNumber;
	private Integer pin;
	
	public Credentials() {}
	
	public Credentials(Long accountNum, Integer pin) {
		super();
		this.accountNumber = accountNum;
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
