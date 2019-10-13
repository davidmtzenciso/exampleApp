package exercise.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import exercise.com.app.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
