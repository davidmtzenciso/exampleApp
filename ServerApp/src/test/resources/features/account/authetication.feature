Feature: user authentication 

	Scenario Outline: <testCase> <expectedResult>
	
	Background:
       Given there is an account with id 1 and pin 1234
	
		Given user provides the values
		| accountNumber | pin   | 
		| <id> 					| <pin> |
		
		When user wants to be authenticated  '<testCase>'
		Then authentication '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id 				| pin 	| 
			| ACCOUNT NUMBER DOES'T MATCH  | FAILS 					| 2					| 1234  | 
			| PIN DOESN'T MATCH						 | FAILS					| 1					| 1111	|
			| ACCOUNT NUMBER AND PIN MATCH | SUCCEEDS 	 		| 1					| 1234  |
			