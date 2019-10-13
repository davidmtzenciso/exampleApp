package exercise.com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import exercise.com.app.model.Account;
import exercise.com.app.service.AccountService;

@RestController
@RequestMapping("/debits-check")
public class DebitsCheckController {

	@Autowired
	private AccountService accountService;
	
	
	@RequestMapping(value="/account", method=RequestMethod.POST, 
					consumes="application/json", produces="application/json")
	@ResponseBody Account createAccount(@RequestBody Account account) {
		return this.accountService.createAccount(account);
	}
	
	
}
