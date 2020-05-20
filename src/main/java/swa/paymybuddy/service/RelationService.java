package swa.paymybuddy.service;

import swa.paymybuddy.model.Relation;

public interface RelationService 
{
	boolean addUserToMyNetwork(int userId) throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException;
	
	Relation addUserToMyNetworkForCredit(int myFriendUserId) throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException;
	
	Relation addUserToMyNetworkForDebit(int myFriendUserId) throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException;
}
