package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import swa.paymybuddy.model.User;
import swa.paymybuddy.service.UserService;

@SpringBootTest
public class UserServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;
	
	@Test
	public void givenNewUser_whenRegisterInternal_thenUserIsSavedInDatabase()
	{
		// GIVEN
		String email = "email_1";
		// WHEN
		User user = userService.registerUserInternal(email, passwordClear, true);
		// THEN
		assertTrue(bCryptPasswordEncoder.matches(passwordClear, user.getPassword()));
	}

}
