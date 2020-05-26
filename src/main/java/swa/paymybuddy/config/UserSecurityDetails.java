package swa.paymybuddy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.User;
import swa.paymybuddy.service.UserService;

@Service
public class UserSecurityDetails implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		if (email.trim().isEmpty()) {
			throw new UsernameNotFoundException("email address is empty");
		}

		User user = userService.getUserByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("User " + email + " not found");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getGrantedAuthorities(user));
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(User.ROLE_APP_USER));
		return authorities;
	}
}