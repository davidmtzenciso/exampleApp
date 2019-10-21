package com.example.external.app;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/external")
public class Controller {
	
	private Long idCount;
	
	private Controller() {
		idCount = new Long(1000000);
	}

	@RequestMapping(value = "/debits-checks", method = RequestMethod.GET, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Long createAccount(@RequestParam Double amount, @RequestParam Integer type, @RequestParam String description) {
		return idCount++;
	}
}
