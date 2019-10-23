Feature: user authentication 

	Scenario Outline: if <testCase> authentication <expectedResult>
	
		Given user provides the values <id> <pin>

		When user '<testCase>'
		
		Then authentication '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id 				| pin 	| 
			| ACCOUNT NUMBER DOESNT MATCH  | FAILS 					| 1231231		| 1234  | 
			| PIN DOESNT MATCH						 | FAILS					| 1					| 1111	|
			| ACCOUNT NUMBER AND PIN MATCH | SUCCEEDS 	 		| 1					| 1234  |
			