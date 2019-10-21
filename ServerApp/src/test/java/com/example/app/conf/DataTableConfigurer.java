package com.example.app.conf;

import java.util.Locale;
import java.util.Map;

import com.example.app.model.Account;

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
						 			entry.get("accountHoldersId"), Double.parseDouble(entry.get("balance")));
			}
		}));
	}
	
	

}