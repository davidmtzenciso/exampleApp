Feature: Delete an Account 

	Scenario Outline: <testCase> <expectedResult>
	
	Background:
       Given there is an account with id 1
	 
		Given user wants to delete his account by providing
		|  id  |
		| <id> |
		
		When user wants to delete the delete account '<testCase>'
		
		Then delete '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id | 
			| WITH NON EXISTING ID				 | FAILS				  | 2  |
			| WITH EMPTY ID								 | FAILS					|    |
			| WITH EXISTING ID						 | SUCCEEDS      	| 1  | 
