package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.model.Transaction;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
