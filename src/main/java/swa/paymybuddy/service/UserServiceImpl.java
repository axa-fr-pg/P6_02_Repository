package swa.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User getUserByEmail(String email) {
		for (User u : userRepository.findAll()) {
			if (u.getEmail().equals(email)) return u;
		}
		return null;
	}

	@Override
	public User registerUserInternal(String email, String password, boolean permanent) 
	{	
	    User user = new User(0, 0, email, bCryptPasswordEncoder.encode(password), permanent);
	    return userRepository.save(user);
	}

	@Override
	public User registerUserSocialNetwork(int networkCode, String email, String password, boolean permanent) 
	{	
	    User user = new User(0, networkCode, email, bCryptPasswordEncoder.encode(password), permanent);
	    return userRepository.save(user);
	}
	
}
