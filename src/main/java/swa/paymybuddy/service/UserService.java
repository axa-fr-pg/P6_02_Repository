package swa.paymybuddy.service;

import swa.paymybuddy.model.User;

public interface UserService {
	
	User getUserByEmail(String email);
	
	User getAuthenticatedUser();
	
	User registerUserInternal(User user);

	User registerUserSocialNetwork(User user) throws InvalidSocialNetworkCodeException;
}
