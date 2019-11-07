package com.example.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.Account;
import com.example.app.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	public Optional<Page<Transaction>> findByAccount(Account account, Pageable pageable);
}
