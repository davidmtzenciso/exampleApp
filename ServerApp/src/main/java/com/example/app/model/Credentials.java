package com.example.app.model;

import java.io.Serializable;

<<<<<<< HEAD
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

=======
>>>>>>> refs/heads/master
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

<<<<<<< HEAD
import lombok.Data;
import lombok.NoArgsConstructor;
=======
@Component
@Scope("request")
public class Credentials implements Serializable  {
>>>>>>> refs/heads/master

@Component
@Scope("request")
public class Credentials implements Serializable  {
	
	@NotNull(message="unable to process, no account number was provided")
	@Min(message="unable to process, account number cannot be cero", value=1)
	private Long accountNumber;
	
	@NotNull(message="unable to process, no pin was provided")
	@Min(message="unable to process, PIN number cannot be cero", value=1)
	private Integer pin;
	
	@JsonIgnore
	private static final long serialVersionUID = 7031738121556269844L;
	
	

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
