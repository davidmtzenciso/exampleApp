package exercise.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.com.app.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	Account findByIdAndPin(Long id, Integer pin);

}
