



# Security

### Database password encryption

java -cp C:\Users\s609998\.m2\repository\org\jasypt\jasypt\1.9.2\jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="your_database_password" password=SecretEncryptionKey algorithm=PBEWithMD5AndDES
