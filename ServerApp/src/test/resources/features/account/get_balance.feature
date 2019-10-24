Feature: Get account balance

	Scenario Outline: <testCase> get the balance <expectedResult>
	 
		Given user provides the id <id> of the account to query
		
		When user wants to get his account balance '<testCase>'
		
		Then the balance query '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id       | 
			| WITH NON EXISTING ID				 | FAILS				  | 2234234  |
			| WITH EMPTY ID								 | FAILS					| 0        |
			| WITH EXISTING ID						 | SUCCEEDS      	| 1 			 | 