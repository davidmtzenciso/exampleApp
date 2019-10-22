Feature: user authentication 

	Background:
       Given there is an account with id 1 and pin 1234

	Scenario Outline: <testCase> <expectedResult>
	
		Given user provides the values <id> <pin>

		When user '<testCase>'
		
		Then authentication '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id 				| pin 	| 
			| ACCOUNT NUMBER DOESNT MATCH  | FAILS 					| 0					| 1234  | 
			| PIN DOESNT MATCH						 | FAILS					| 1					| 1111	|
			| ACCOUNT NUMBER AND PIN MATCH | SUCCEEDS 	 		| 1					| 1234  |
			