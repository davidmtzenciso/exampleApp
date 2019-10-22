Feature: Delete an Account 

	Background:
       Given there is an account with id 2

	Scenario Outline: <testCase> <expectedResult>
	 
		Given user provides the value <id>
		
		When user wants to delete the account '<testCase>'
		
		Then delete '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id | 
			| WITH NON EXISTING ID				 | FAILS				  | 1  |
			| WITH EMPTY ID								 | FAILS					|    |
			| WITH EXISTING ID						 | SUCCEEDS      	| 2  | 
