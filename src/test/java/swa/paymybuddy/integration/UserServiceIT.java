package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.PersistentLoginsRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.UserService;

@SpringBootTest
public class UserServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
    private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@BeforeEach
	public void setup() 
	{
		userRepository.deleteAll();
	}

	@Test
	public void givenNewUser_whenRegisterInternal_thenUserIsSavedInDatabase()
	{
		// GIVEN
		String email = "email_1";
		// WHEN
		User user = userService.registerUserInternal(email, passwordClear);
		// THEN
		assertTrue(bCryptPasswordEncoder.matches(passwordClear, user.getPassword()));
	}

	@Test
	public void givenTwoNewUserWithSameEmail_whenRegisterInternal_thenExceptionIsRaised()
	{
		// GIVEN
		String email = "email_2";
		// WHEN
		userService.registerUserInternal(email, passwordClear);
		// THEN
	    assertThrows(DataIntegrityViolationException.class, () -> 
	    	userService.registerUserInternal(email, passwordClear));
	}

}
