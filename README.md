# Pay My Buddy

Pay My Buddy is an application to manage easily transfers within your friends and family network.
Currently there is almost no client pages to access it, nor APIs.
But the next release will contain them.

## Access the application

### User registration
A page has been designed to register new users : http://localhost:8080/register.html
Please note that the application is installed with a few predefined users.
One of them is the user to collect all transfer commissions (if this user is deleted the application will raise a NoCommissionUserException)

### User login
A page has been designed to log into the application, based on Spring Security : http://localhost:8080/login.html
You can choose to remain connected by activating the "Remember me" checkbox.

### Welcome page
An empty page is currently delivered, where user land after login.
This page will be completed with a menu to access all application features in a future release.

## Available features after login

### Transfer money from or to an external account of mine

Methods in TransferService : 

		Transfer transferFromOutside(Transfer transfer)
		Transfer transferToOutside(Transfer transfer)

Exceptions thrown :
		NoAuthenticatedUserException, if you call it without logging into the application beforehand
	 	InvalidTransferAmountException, if you try to transfer negative or null amounts
		TransferAmountGreaterThanAccountBalanceException, if your balance is not sufficient

### Add someone to my network

Methods in RelationService : 

		boolean addUserToMyNetwork(int userId) 

Exceptions thrown :
	 	NoAuthenticatedUserException, if you call it without logging into the application beforehand
		IllegalOperationOnMyOwnUserException, if you try to add yourself to your network
		RelationAlreadyExistsException, if you add someone who is already in your network

### Transfer money to someone in my network

Methods in TransferService : 

		Transfer transferInternal(Transfer transfer) 

Exceptions thrown :
		NoAuthenticatedUserException, if you call it without logging into the application beforehand
	 	TransferOutsideOfMyNetworkException, if you try to transfer money to someone outside of your network (hacking with SQL injection ?)
	 	TransferAmountGreaterThanAccountBalanceException, if your balance is not sufficient
		InvalidTransferAmountException, if you try to transfer negative or null amounts

### Display all my transfers

Methods in TransferService : 

		ArrayList<Transfer> getMyTransferList() 

Exceptions thrown :
		NoAuthenticatedUserException, if you call it without logging into the application beforehand

### Commissions are collected

Please note that, event though this is no feature for the end user, the application collects commissions for each transfer.
The fee is calculated by the calculateCommission method in TransferService (0.5%).

# Security

### Database password encryption

To avoid unexpected database accesses the database connection password is encrypted with JASYPT and the key is hidden in the environment variable JASYPT_ENCRYPTOR_PASSWORD. To generate an encrypted password use the following command : 
java -cp C:\Users\s609998\.m2\repository\org\jasypt\jasypt\1.9.2\jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="your_raw_database_password" password=YourSecretEncryptionKeyToBeStoredInTheEnvVariable algorithm=PBEWithMD5AndDES

### Login password encryption

To avoid unexpected money transfers, application user passwords are also encrypted, with BCrypt, based on the Spring Security plugin.
As a consequence, the database does not contain any raw password.

# Performance

## Email address is a natural id
From a business prospective, email address should be the primary key for the user table.
But this would lead to joining multiple tables based on a string index, which is a bad architectural choice.
As a consequence, a technical user id has been introduced, even though is isn't really mandatory.

### Indexes
Indexes have been defined for all foreign keys, even if this is not required by any constraint.
This eases any query in the database.

### Database column size
All columns have been designed to fit precisely to the business needs.
For example the user password has size 60, which is the encrypted password size.
The user and account types have been defined as tiny integer, because they can only have a few different values.

# Database UML diagram

![](https://github.com/philippegeyaxa/P6_02_Repository/blob/develop/resources/database_uml_diagram.jpg?raw=true)


