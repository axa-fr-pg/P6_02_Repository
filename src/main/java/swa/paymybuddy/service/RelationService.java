package swa.paymybuddy.service;

import swa.paymybuddy.model.Relation;

public interface RelationService 
{
	boolean addUserToMyNetwork(int userId) throws NoAuthenticatedUserException;
	
	Relation addUserToMyNetworkForCredit(int myFriendUserId) throws NoAuthenticatedUserException;
	
	Relation addUserToMyNetworkForDebit(int myFriendUserId) throws NoAuthenticatedUserException;
}
