package swa.paymybuddy.service;

import swa.paymybuddy.model.Link;

public interface LinkService 
{
	boolean addUserToMyNetwork(int userId);
	
	Link addUserToMyNetworkForCredit(int myFriendUserId);
	
	Link addUserToMyNetworkForDebit(int myFriendUserId);
}
