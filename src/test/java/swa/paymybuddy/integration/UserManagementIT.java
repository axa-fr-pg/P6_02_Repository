package swa.paymybuddy.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@SpringBootTest
public class UserManagementIT {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
    private UserRepository userRepository;

	private MockMvc mvc;
	
	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	public void givenValidCredentials_whenLogin_thenUserIsAuthenticated() throws Exception
	{
		// GIVEN
		String email = "email_1";
		userRepository.save(new User(email, passwordCrypted, 0));
		// WHEN & THEN
		mvc.perform(formLogin("/login").user(email).password(passwordClear)).andExpect(authenticated());
	}

	@Test
	public void givenWrongCredentials_whenLogin_thenUserIsNotAuthenticated() throws Exception
	{
		// GIVEN
		String email = "email_2";
		userRepository.save(new User(email, "wrong", 0));
		// WHEN & THEN
		mvc.perform(formLogin("/login").user(email).password(passwordClear)).andExpect(unauthenticated());
	}

	@Test
	public void givenNewUser_whenRegister_thenUserIsSavedInDatabase() throws Exception
	{
		// GIVEN
		String email = "email_3";
		String form = "email=" + email + "&password=" + passwordClear;
		
		mvc.perform( MockMvcRequestBuilders
				.post("/register")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		        .content(form)
		        .accept(MediaType.APPLICATION_FORM_URLENCODED))
		        .andExpect(status().isCreated());
	}

}
