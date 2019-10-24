package com.example.app.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Component
@Scope("prototype")
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private Long id;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="date_", nullable=false, length=10)
	private Date date;
	
	@Column(name="type_", nullable=false)
	private Integer type;
	
	@Column(nullable=false)
	private Double amount;
	
	@Column(nullable=false, length=50)
	private String description;
	
	@JsonInclude(Include.NON_NULL)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="account_id")
	@JsonIdentityInfo(
	  generator = ObjectIdGenerators.PropertyGenerator.class, 
	  property = "id")
	private Account account;
	
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	public Transaction() {}
	
	public Transaction(Long id, Date date, Integer type, Double amount, String description, Account account) {
		this.id = id;
		this.date = date;
		this.type = type;
		this.amount = amount;
		this.description = description;
		this.account = account;
	}

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
