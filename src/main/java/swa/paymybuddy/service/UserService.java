package swa.paymybuddy.service;

import swa.paymybuddy.model.User;

public interface UserService {
	
	User getUserByEmail(String email);
	
	User getAuthenticatedUser() throws NoAuthenticatedUserException;
	
	User registerUserInternal(User user);

	User registerUserSocialNetwork(User user) throws InvalidSocialNetworkCodeException;
}
