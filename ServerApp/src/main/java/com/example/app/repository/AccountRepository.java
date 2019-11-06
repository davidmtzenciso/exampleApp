package com.example.app.repository;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.example.app.model.Account;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface AccountRepository extends JpaRepository<Account, Long>{
	
	public Optional<Account> findByIdAndPin(Long id, Integer pin);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	@Query("SELECT A FROM Account A WHERE A.id = :id")
	public Optional<Account> findAndLockById(@Param("id") Long id);
			
}
