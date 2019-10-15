package com.example.app.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Scope("prototype")
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false, length=10)
	private Date date;
	
	@Column(nullable=false)
	private Integer type;
	
	@Column(nullable=false)
	private Double amount;
	
	@Column(nullable=false, length=50)
	private String description;
	
	@JoinColumn(name="account_id")
	@JsonBackReference
	private Account account;
	
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
