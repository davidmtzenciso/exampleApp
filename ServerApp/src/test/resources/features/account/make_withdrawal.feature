Feature: make a withdrawal

	Scenario Outline: <testCase> make a withdrawal <expectedResult>
	 
		Given user provides the values to withdrawal funds
		| id   | date   |  type  |  amount  | description   |  account  |
		| <id> | <date> | <type> | <amount> | <description> | <account> |
		
		When user wants to make a withdrawal '<testCase>'
		Then the withdrawal '<expectedResult>'
		
		Examples:
			| testCase                  	      								  | expectedResult | id |  date        | type  | amount    |  description		 | account |
			| WITHOUT ACCOUNT ID        	    								 	  | FAILS					 |    |  2019-09-12  |   3   |  1000.0	 |  ATM av america |         |
			| WITH NON EXISTENT ACCOUNT ID  					 				    | FAILS					 | 	  |  2019-09-12  |   3   |  1000.0	 |  ATM av america | 112312  |
			| WITH EXISTING ACCOUNT ID BUT WITHOUT ENOUGH FUNDS   | FAILS					 | 	  |  2019-09-12  |   3   |  20000.0  |  ATM av america | 1       |
			| WITH EXISTING ACCOUNT ID AND ENOUGH FUNDS   		  	| SUCCEEDS			 |    |  2019-09-12  |   3   |  100.0	   |  ATM av america | 1       |

