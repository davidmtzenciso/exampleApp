package com.example.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.app.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Scope("prototype")
@Entity
@Table(name="account")
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="account_id", insertable=false, updatable=false, nullable=false)
	private Long id;
	
	@Min(message= "unable to process, pin cannot be 0 or below", value = 1)
	@NotNull(message="unable to process, no pin number was provided")
	@Column(nullable=false)
	private Integer pin;
		
	@NotEmpty(message = "Please provide a first name")
	@NotNull(message = "Please provide a first name")
	@Column(name="first_name", nullable=false, length=40)
	private String firstName;
	
	@NotEmpty(message = "Please provide a last name")
	@NotNull(message = "Please provide a last name")
	@Column(name="last_name", nullable=false, length=60)
	private String lastName;

	@Size(message="unable to process, account's holder id its too long", max=10)
	@Column(name="account_holders_id", nullable=false, length=10)
	private String accountHoldersId;

	@Min(message="unable to process, balance cannot have a negative value", value=0)
	@Column(nullable=false)
	private Double balance;
	
	@JsonInclude(Include.NON_EMPTY)
	@JsonSerialize(as=ArrayList.class)
	@JsonDeserialize(as=ArrayList.class)
	@OneToMany(mappedBy="account", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Transaction> transactions;
	
	@JsonIgnore
	private static final long serialVersionUID = -1051181485163125999L;
	
	public Account( ) {}
	
	public Account(Long id, Integer pin, String firstName, String lastName, String accountHoldersId, Double balance, List<Transaction> transactions) {
		this.id = id;
		this.pin = pin;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountHoldersId = accountHoldersId;
		this.balance = balance;
		this.transactions = transactions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
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

	public String getAccountHoldersId() {
		return accountHoldersId;
	}

	public void setAccountHoldersId(String accountHoldersId) {
		this.accountHoldersId = accountHoldersId;
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
