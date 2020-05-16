package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import swa.paymybuddy.controller.UserController;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User getUserByEmail(String email) {
		logger.info("getUserByEmail " + email);
		for (User u : userRepository.findAll()) {
			if (u.getEmail().equals(email)) return u;
		}
		return null;
	}
	
	@Override
	public User getAuthenticatedUser()
	{
		logger.info("getAuthenticatedUser ");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return getUserByEmail(authentication.getName());
	}

	@Override
	public User registerUserInternal(String email, String password) 
	{	
		logger.info("registerUserInternal " + email);
	    User user = new User(0, 0, email, bCryptPasswordEncoder.encode(password));
	    return userRepository.save(user);
	}

	@Override
	public User registerUserSocialNetwork(int networkCode, String email, String password) 
	{	
		logger.info("registerUserSocialNetwork " + email);
	    User user = new User(0, networkCode, email, bCryptPasswordEncoder.encode(password));
	    return userRepository.save(user);
	}
}
