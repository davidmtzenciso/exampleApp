Feature: Delete an Account 

	Scenario Outline: <testCase> delete <expectedResult>
	 
		Given user provides the id <id> of the account to delete
		
		When user wants to delete the account '<testCase>'
		
		Then delete '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id       | 
			| WITH NON EXISTING ID				 | FAILS				  | 2234234  |
			| WITH EMPTY ID								 | FAILS					| 0        |
			| WITH EXISTING ID						 | SUCCEEDS      	| 2 			 | 
