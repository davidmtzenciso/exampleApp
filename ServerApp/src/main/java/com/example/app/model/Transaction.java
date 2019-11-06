package com.example.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.app.model.Account;
import com.example.app.model.Transaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@Scope("request")
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private Long id;

	@NotNull(message="unable to process, date was not provided")
	@NotEmpty(message="unable to process, date was not provided")
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="date_", nullable=false, length=10)
	private Date date;

	@Min(message="unable to process, invalid transaction type", value=1)
	@Max(message="unable to process, invalid transcation type", value=4)
	@Column(name="type_", nullable=false)
	private Integer type;
	
	@Min(message="unable to process, the ammount cannot be a negative value", value=0)
	@Column(nullable=false)
	private Double amount;

	@Size(message="unable to process, description too long", max=50)
	@Column(nullable=false, length=50)
	private String description;

	@NotNull(message="unable to process, account info not provided")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="account_id")
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
