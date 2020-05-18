package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

import java.math.BigDecimal;

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

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.AccountService;

@SpringBootTest
public class AccountServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
    private AccountService accountService;

	@Autowired
    private AccountRepository accountRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private WebApplicationContext context;
	
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

	private MockMvc mvc;

	@BeforeEach
	public void setup() 
	{
		accountRepository.deleteAll();
		userRepository.deleteAll();
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
	}
	
	@Test
	public void givenAuthenticated_whenAddInternalAccount_thenAccountIsCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		int myUserId = userRepository.save(new User(0, 0, myEmail, passwordCrypted)).getId();
		SecurityContext securityContext = (SecurityContext) mvc
				.perform(formLogin("/login").user(myEmail).password(passwordClear))
				.andExpect(authenticated())
				.andReturn().getRequest().getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(securityContext);
		// WHEN
		Account account = accountService.addInternal(myUserId);
		// THEN
		assertNotNull(account);
		assertEquals(myUserId, account.getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, account.getType());
		assertEquals(new BigDecimal(0), account.getBalance());
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}
	
	@Test
	public void givenAuthenticated_whenAddExternalAccount_thenAccountIsCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		int myUserId = userRepository.save(new User(0, 0, myEmail, passwordCrypted)).getId();
		SecurityContext securityContext = (SecurityContext) mvc
				.perform(formLogin("/login").user(myEmail).password(passwordClear))
				.andExpect(authenticated())
				.andReturn().getRequest().getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(securityContext);
		// WHEN
		Account account = accountService.addExternal(myUserId);
		// THEN
		assertNotNull(account);
		assertEquals(myUserId, account.getUser().getId());
		assertEquals(Account.TYPE_EXTERNAL, account.getType());
		assertEquals(new BigDecimal(0), account.getBalance());
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}
}
