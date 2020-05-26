package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.exception.InvalidSocialNetworkCodeException;
import swa.paymybuddy.service.exception.NoAuthenticatedUserException;

@Service
@Transactional(rollbackFor = Exception.class)
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
	public User getAuthenticatedUser() throws NoAuthenticatedUserException
	{
		logger.info("getAuthenticatedUser ");
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) throw new NoAuthenticatedUserException();
		Authentication authentication = context.getAuthentication();
		if (authentication == null) throw new NoAuthenticatedUserException();
		return getUserByEmail(authentication.getName());
	}

	private User registerUser(User user)
	{
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    return userRepository.save(user);
	}
	
	@Override
	public User registerUserInternal(User user) 
	{	
		logger.info("registerUserInternal " + user.getEmail());
		user.setType(0); // Internal means type 0
		return registerUser(user);
	}

	@Override
	public User registerUserSocialNetwork(User user) throws InvalidSocialNetworkCodeException 
	{	
		logger.info("registerUserSocialNetwork " + user.getEmail());
		if (user.getType() < 1) throw new InvalidSocialNetworkCodeException(); // 0 reserved for internal users
		return registerUser(user);
	}
}
