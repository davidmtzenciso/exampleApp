package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
