package com.example.app.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.app.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Component
@Scope("prototype")
@Entity
@Table(name="account")
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="account_id", insertable=false, updatable=false, nullable=false)
	private Long id;
	
	@Column(nullable=false)
	private Integer pin;
		
	@Column(name="first_name", nullable=false, length=40)
	private String firstName;
	
	@Column(name="last_name", nullable=false, length=60)
	private String lastName;
	
	@Column(name="account_holders_id", nullable=false)
	private String accountHoldersId;
	
	@Column(nullable=false)
	private Double balance;
	
	@JsonInclude(Include.ALWAYS)
	@OneToMany(mappedBy="account", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	private Set<Transaction> transactions;
	
	@JsonIgnore
	private static final long serialVersionUID = -1051181485163125999L;
		
	public Account() {}

	public Account(Long id, Integer pin, String firstName, String lastName, String accountHoldersId, Double balance) {
		this.id = id;
		this.pin = pin;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountHoldersId = accountHoldersId;
		this.balance = balance;
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

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	@JsonDeserialize(as=LinkedHashSet.class)
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
}
