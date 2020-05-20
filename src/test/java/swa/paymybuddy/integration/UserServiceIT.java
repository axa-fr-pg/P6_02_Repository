package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.InvalidSocialNetworkCodeException;
import swa.paymybuddy.service.UserService;

@SpringBootTest
public class UserServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private TestService testService;

	private MockMvc mvc;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
    private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private WebApplicationContext context;
	
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

	@BeforeEach
	public void setup() 
	{
		testService.cleanAllTables();
		mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
	}

	@Test
	public void givenNewUser_whenRegisterInternal_thenUserIsSavedInDatabase()
	{
		// GIVEN
		String email = "email_1";
		// WHEN
		User user = userService.registerUserInternal(new User(0, 0, email, passwordClear));
		// THEN
		assertTrue(bCryptPasswordEncoder.matches(passwordClear, user.getPassword()));
	}

	@Test
	public void givenNewUser_whenRegisterSocialNetwork_thenUserIsSavedInDatabase() throws Exception
	{
		// GIVEN
		String email = "email_2";
		// WHEN
		User user = userService.registerUserSocialNetwork(new User(0, 1, email, passwordClear));
		// THEN
		assertTrue(bCryptPasswordEncoder.matches(passwordClear, user.getPassword()));
	}

	@Test
	public void givenTwoNewUserWithSameEmail_whenRegisterInternal_thenExceptionIsRaised()
	{
		// GIVEN
		String email = "email_3";
		// WHEN
		userService.registerUserInternal(new User(0, 0, email, passwordClear));
		// THEN
	    assertThrows(DataIntegrityViolationException.class, () -> 
	    	userService.registerUserInternal(new User(0, 0, email, passwordClear))
	    );
	}

	@Test
	public void givenLoggedInUser_whenGetAuthenticatedUser_returnsCorrectUser() throws Exception
	{
		// GIVEN
		String email = "email_4";
		userRepository.save(new User(0, 0, email, passwordCrypted));
		SecurityContext securityContext = (SecurityContext) mvc
			.perform(formLogin("/login").user(email).password(passwordClear))
			.andReturn().getRequest().getSession()
			.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(securityContext);
		// WHEN
		User user = userService.getAuthenticatedUser();
		// THEN
		assertNotEquals(0, user.getId());
		assertEquals(email, user.getEmail());
		assertEquals(passwordCrypted, user.getPassword());
		assertEquals(0, user.getType());
	}
}
