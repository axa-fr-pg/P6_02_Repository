package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.PersistentLogins;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.PersistentLoginsRepository;
import swa.paymybuddy.repository.UserRepository;

@SpringBootTest
public class UserControllerIT {

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private TestService testService;

	@Autowired  
	private PersistentLoginsRepository persistentLoginsRepository;
	
	private MockMvc mvc;
	
	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";
	
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
	public void givenNewUser_whenRegister_thenUserIsSavedInDatabase() throws Exception
	{
		// GIVEN
		String email = "email_1";
		String form = "email=" + email + "&password=" + passwordClear;
		
		mvc.perform( post("/register")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		        .content(form)
		        .accept(MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().isCreated());
	}	

	@Test
	public void givenValidCredentials_whenLogin_thenUserIsAuthenticatedWithoutToken() throws Exception
	{
		// GIVEN
		String email = "email_2";
		userRepository.save(new User(0, 0, email, passwordCrypted));
		// WHEN & THEN
		mvc.perform(formLogin("/login").user(email).password(passwordClear)).andExpect(authenticated());
		boolean tokenSaved = false;
		for (PersistentLogins p : persistentLoginsRepository.findAll()) if (p!=null) tokenSaved = true;
		assertFalse(tokenSaved);
	}

	@Test
	public void givenWrongCredentials_whenLogin_thenUserIsNotAuthenticated() throws Exception
	{
		// GIVEN
		String email = "email_3";
		userRepository.save(new User(0, 0, email, "wrong"));
		// WHEN & THEN
		mvc.perform(formLogin("/login").user(email).password(passwordClear)).andExpect(unauthenticated());
	}

	@Test
	public void givenValidCredentials_whenLoginWithRememberMe_thenPersistentLoginIsSaved() throws Exception
	{
		// GIVEN
		String email = "email_4";
		userRepository.save(new User(0, 0, email, passwordCrypted));
		String form = "username=" + email + "&password=" + passwordClear + "&remember-me=on";
		// WHEN
		mvc.perform( post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		        .content(form)
		        .accept(MediaType.APPLICATION_FORM_URLENCODED)
		);
		boolean tokenSaved = false;
		for (PersistentLogins p : persistentLoginsRepository.findAll()) if (p!=null) tokenSaved = true;
		// THEN
		assertTrue(tokenSaved);
	}
}
