package exercise.com.app.model;

import java.io.Serializable;
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

import exercise.com.app.model.Transaction;

@Component
@Scope("prototype")
@Entity
@Table(name="account")
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="account_id", insertable=false, updatable=false, nullable=false)
	private Long id;
	
	@Column(name="first_name", nullable=false, length=40)
	private String firstName;
	
	@Column(name="last_name", nullable=false, length=60)
	private String lastName;
	
	@Column(nullable=false)
	private Integer pin;
	
	@Column(name="account_holders_id", nullable=false)
	private String accountHoldersId;
	
	@Column(nullable=false)
	private Double balance;
	
	@OneToMany(mappedBy="account", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Set<Transaction> transactions;
	
	private static final long serialVersionUID = 1L;
	
	public Account() {}

	public Account(Long id, String firstName, String lastName, Integer pin, String accountHoldersId, Double balance) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.pin = pin;
		this.accountHoldersId = accountHoldersId;
		this.balance = balance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
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

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
}
