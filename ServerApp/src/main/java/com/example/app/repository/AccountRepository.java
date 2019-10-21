package com.example.app.repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	public Account findByIdAndPin(Long id, Integer pin);
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	@Query("SELECT A FROM Account A WHERE A.id = ?1")
	public Account findAndLockById(Long id);
		
}
