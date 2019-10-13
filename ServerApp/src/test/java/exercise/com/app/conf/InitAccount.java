package exercise.com.app.conf;

import exercise.com.app.model.Account;

public interface InitAccount {

	default Account initialize(Account account) {
		account.setId(null);
		account.setFirstName("david");
		account.setLastName("Martinez Enciso");
		account.setPin(1234);
		account.setBalance(1000.0);
		account.setAccountHoldersId("12345678");
		account.setTransactions(null);
		return account;
	}
}
