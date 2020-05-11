package swa.paymybuddy.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@SpringBootTest
public class UserSecurityConfigIT {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
    private UserRepository userRepository;

	private MockMvc mvc;

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
		String user = "user";
		String passwordClear = "password";
		String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";
		userRepository.save(new User(user, passwordCrypted));
		// WHEN & THEN
		mvc.perform(formLogin("/login").user(user).password(passwordClear)).andExpect(authenticated());
	}
	
}
