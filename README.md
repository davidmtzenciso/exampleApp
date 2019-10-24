# exampleApp

The application was wrote in java 8 and needs Maven 

Execute the goal install for both applications
To run the server execute the goal spring-boot:run
To run the client execute the command java -jar projectdir/ClientApp/target/clientApp-1.0.0-SNAPSHOT

User data loaded, account number = 1, pin = 1234

1-.Open account
2-.log in
3-.Exit

this is the main menu, the user has the options to log in or open an account.
if the user opens an account, he still needs to log in to manipulate it.
If the user chooses the option Exit, the application closes.

1-.Make Deposit
2-.Withdraw funds
3-.Account's Balance
4-.External Operation
5-.Close Account
6-.Exit
Operation: 

Once logged in, the user can do this 5 operations, if the users chooses to close the account, the menu will be close and it will
return to the main menu. For each operation the user will be notify of if the operation was either successful or if it failed and why.

