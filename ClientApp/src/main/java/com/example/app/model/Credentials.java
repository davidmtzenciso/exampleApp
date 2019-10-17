package com.example.app.model;

import java.io.Serializable;

public class Credentials implements Serializable {

	private static final long serialVersionUID = -2468920800107022005L;
	private Long accountNum;
	private Integer pin;
	
	public Credentials() {}
	
	public Credentials(Long accountNum, Integer pin) {
		super();
		this.accountNum = accountNum;
		this.pin = pin;
	}
	public Long getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(Long accountNum) {
		this.accountNum = accountNum;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	
	
}
