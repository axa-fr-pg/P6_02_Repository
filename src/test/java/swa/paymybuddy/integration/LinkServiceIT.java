package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.LinkId;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.LinkRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.LinkService;
import swa.paymybuddy.service.UserService;
import swa.paymybuddy.service.UserServiceImpl;

@SpringBootTest
public class LinkServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private LinkRepository linkRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private WebApplicationContext context;
	
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

	private MockMvc mvc;

	@Autowired
	private UserService userService;

	@Autowired
	private LinkService linkService;

	@BeforeEach
	public void setup() 
	{
		linkRepository.deleteAll();
		userRepository.deleteAll();
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
	}
	
	@Test
	public void givenAuthenticated_whenAddToMyNetwork_thenTwoNewLinksCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		int myUserId = userRepository.save(new User(0, 0, myEmail, passwordCrypted)).getId();
		int myFriendId = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used")).getId();
		SecurityContext securityContext = (SecurityContext) mvc
				.perform(formLogin("/login").user(myEmail).password(passwordClear))
				.andExpect(authenticated())
				.andReturn().getRequest().getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(securityContext);
		// WHEN
		boolean result = linkService.addUserToMyNetwork(myFriendId);
		// THEN
		assertEquals(true, result);
		assertFalse(linkRepository.findById(new LinkId(myUserId, myFriendId)).isEmpty());
		assertFalse(linkRepository.findById(new LinkId(myFriendId, myUserId)).isEmpty());
	}
	
	@Test
	public void givenNotAuthenticated_whenAddToMyNetwork_thenTwoNewLinksCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		int myUserId = userRepository.save(new User(0, 0, myEmail, passwordCrypted)).getId();
		int myFriendId = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used")).getId();
		mvc.perform(logout()).andExpect(unauthenticated());
		// WHEN
		Exception result = null;
		try {
			linkService.addUserToMyNetwork(myFriendId);
		} catch (Exception e) {
			result = e;
		}
		// Should never be reached
		assertNotNull(result);
		assertEquals(NullPointerException.class, result.getClass());
		assertEquals("swa.paymybuddy.service.UserServiceImpl", result.getStackTrace()[0].getClassName());
		assertEquals("getAuthenticatedUser", result.getStackTrace()[0].getMethodName());
	}
}
