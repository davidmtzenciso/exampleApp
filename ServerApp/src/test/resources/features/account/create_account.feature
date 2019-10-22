Feature: Create an Account 

	Scenario Outline: <testCase> create the account <expectedResult>
	 
		Given user wants to create an account with the values
		| id   | firstName   | lastName   | pin   | accountHoldersId   | balance   |
		| <id> | <firstName> | <lastName> | <pin> | <accountHoldersId> | <balance> |
		
		When user wants to save the new account '<testCase>'
		Then save '<expectedResult>'
		
		Examples:
			| testCase                  	 | expectedResult | id | firstName     | lastName           |  pin   | accountHoldersId | balance |
			| WITHOUT FIRST NAME        	 | FAILS					|    |               | Martìnez Enciso	  | 1234	 | 12345678         | 1000.0  |
			| WITHOUT LAST NAME         	 | FAILS					|    |  David        | 								    | 1234	 | 12345678         | 1000.0  |
			| WITH A CERO FOR A PIN        | FAILS					|    |  David        | Martìnez Enciso	  | 0      | 12345678         | 1000.0  |
			| WITHOUT ACCOUNT HOLDERS ID   | SUCCEEDS 			|    |  David        | Martìnez Enciso	  | 1234   | 				          | 1000.0  |
			| WITH ALL REQUIRED FIELDS     | SUCCEEDS				|    |  David        | Martìnez Enciso	  | 1234   | 12345678         | 1000.0  |
			