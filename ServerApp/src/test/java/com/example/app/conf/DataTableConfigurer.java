package com.example.app.conf;

import java.sql.Date;
import java.util.Locale;
import java.util.Map;

import com.example.app.model.Account;
import com.example.app.model.Transaction;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

public class DataTableConfigurer implements TypeRegistryConfigurer {

	@Override
	public Locale locale() {
		return Locale.ENGLISH;
	}

	@Override
	public void configureTypeRegistry(TypeRegistry typeRegistry) {
		typeRegistry.defineDataTableType(new DataTableType(Account.class, new TableEntryTransformer<Account>() {

			@Override
			public Account transform(Map<String, String> entry) throws Throwable {
				 return new Account(entry.get("id").isEmpty() ? null : Long.parseLong(entry.get("id")), 
						 			Integer.parseInt(entry.get ("pin")),entry.get("firstName"), entry.get("lastName"), 
						 			entry.get("accountHoldersId"), Double.parseDouble(entry.get("balance")), null);
			}
		}));
		
		typeRegistry.defineDataTableType(new DataTableType(Transaction.class, new TableEntryTransformer<Transaction>() {

			@Override
			public Transaction transform(Map<String, String> entry) throws Throwable {
				Account account = null;
				
				 if(!entry.get("account").isEmpty()) {
					 account = new Account();
					 account.setId(Long.parseLong(entry.get("account")));
				 }
				 return new Transaction(entry.get("id").isEmpty() ? null : Long.parseLong(entry.get("id")),
						 				Date.valueOf(entry.get("date")), Integer.parseInt(entry.get("type")), 
						 				Double.parseDouble(entry.get("amount")), entry.get("description"), account);
			}
		}));
	}
	
	

}
