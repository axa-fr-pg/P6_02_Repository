package swa.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.exception.InvalidSocialNetworkCodeException;
import swa.paymybuddy.service.exception.NoAuthenticatedUserException;

@SpringBootTest
public class UserServiceTest 
{
	private User u1 = new User(1, 11, "email_1", "password_1");
	private User u2 = new User(2, 12, "email_2", "password_2");
	private User u3 = new User(3, 13, "email_3", "password_3");
	private User u4 = new User(4, 14, "email_4", "password_4");
	private User u4crypted = new User(4, 14, "email_4", "password_crypted_4");
	private User u5 = new User(5, 0, "email_5", "password_5");
	
	@MockBean
	UserRepository userRepository;
	
	@Mock
	SecurityContextHolder contextHolder;
	
	@Mock
	User user;

	@Autowired
	UserService userService;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Test
	void givenMultipleUsers_getUserByEmail_returnsTheRightUser()
	{
		// GIVEN 
		List<User> userList = Arrays.asList(u1, u2, u3);
		when(userRepository.findAll()).thenReturn(userList);
		// WHEN
		User u = userService.getUserByEmail(u2.getEmail());
		// THEN
		assertNotNull(u);
		assertEquals(u2.getId(), u.getId());
		assertEquals(u2.getType(), u.getType());
		assertEquals(u2.getEmail(), u.getEmail());
		assertEquals(u2.getPassword(), u.getPassword());
	}	
	
	@Test
	void givenNewUser_registerUserInternal_returnsTheRightUser()
	{
		// GIVEN
		when(bCryptPasswordEncoder.encode(u4.getPassword())).thenReturn(u4crypted.getPassword());
		when(userRepository.save(u4)).thenReturn(u4crypted);
		// WHEN
		User u = userService.registerUserInternal(u4);
		// THEN
		assertNotNull(u);
		assertEquals(u4crypted.getId(), u.getId());
		assertEquals(u4crypted.getType(), u.getType());
		assertEquals(u4crypted.getEmail(), u.getEmail());
		assertEquals(u4crypted.getPassword(), u.getPassword());
	}
	
	@Test
	void givenNewUser_registerUserSocialNetwork_returnsTheRightUser() throws Exception
	{
		// GIVEN
		when(bCryptPasswordEncoder.encode(u4.getPassword())).thenReturn(u4crypted.getPassword());
		when(userRepository.save(u4)).thenReturn(u4crypted);
		// WHEN
		User u = userService.registerUserSocialNetwork(u4);
		// THEN
		assertNotNull(u);
		assertEquals(u4crypted.getId(), u.getId());
		assertEquals(u4crypted.getType(), u.getType());
		assertEquals(u4crypted.getEmail(), u.getEmail());
		assertEquals(u4crypted.getPassword(), u.getPassword());
	}
	
	@Test
	void givenWrongNetworkCode_registerUserSocialNetwork_throwsException() throws Exception
	{
		// GIVEN 
		// WHEN & THEN
	    assertThrows(InvalidSocialNetworkCodeException.class, () -> 
    		userService.registerUserSocialNetwork(u5)
	    );
	}
	
	@Test
	void givenUnauthenticated_getAuthenticatedUser_throwsException()
	{
		// GIVEN
		// WHEN & THEN
		assertThrows(NoAuthenticatedUserException.class, () ->
			userService.getAuthenticatedUser()
		);
	}
}
