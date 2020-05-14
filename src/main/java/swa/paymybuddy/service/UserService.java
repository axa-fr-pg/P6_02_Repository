package swa.paymybuddy.service;

import swa.paymybuddy.model.User;

public interface UserService {
	
	User getUserByEmail(String email);
	
	User registerUserInternal(String email, String password);

	User registerUserSocialNetwork(int networkCode, String email, String password);
}
