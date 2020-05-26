package swa.paymybuddy.service;

import swa.paymybuddy.service.exception.IllegalOperationOnMyOwnUserException;
import swa.paymybuddy.service.exception.NoAuthenticatedUserException;
import swa.paymybuddy.service.exception.RelationAlreadyExistsException;

public interface RelationService 
{
	boolean addUserToMyNetwork(int userId) throws NoAuthenticatedUserException, 
		IllegalOperationOnMyOwnUserException, RelationAlreadyExistsException;	
}
