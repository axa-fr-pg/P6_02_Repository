package swa.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getUserByEmail(String email) {
		User user = null;
		for (User u : userRepository.findAll()) {
			if (u.getEmail().equals(email)) return u;
		}
		return null;
	}

}
