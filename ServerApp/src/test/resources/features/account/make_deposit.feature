Feature: make a deposit

	Scenario Outline: <testCase> make a deposit <expectedResult>
	 
		Given user provides the values to make a deposit
		| id   | date   |  type  |  amount  | description   |  account  |
		| <id> | <date> | <type> | <amount> | <description> | <account> |
		
		When user wants to make a deposit '<testCase>'
		Then the deposit '<expectedResult>'
		
		Examples:
			| testCase                  	      | expectedResult | id |  date        | type  | amount    |  description				   | account |
			| WITHOUT ACCOUNT ID        	      | FAILS					 |    |  2019-09-12  |   3   |  1000.0	 |  transfer from paypal |         |
			| WITHOUT NON EXISTENT ACCOUNT ID   | FAILS					 | 	  |  2019-09-12  |   3   |  1000.0	 |  transfer from paypal | 112312  |
			| WITH EXISTING ACCOUNT ID        	| SUCCEEDS			 |    |  2019-09-12  |   3   |  1000.0	 |  transfer from paypal | 1       |
