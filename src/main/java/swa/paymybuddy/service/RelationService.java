package swa.paymybuddy.service;

import swa.paymybuddy.model.Relation;

public interface RelationService 
{
	boolean addUserToMyNetwork(int userId);
	
	Relation addUserToMyNetworkForCredit(int myFriendUserId);
	
	Relation addUserToMyNetworkForDebit(int myFriendUserId);
}
