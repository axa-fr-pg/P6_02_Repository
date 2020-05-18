package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

import java.math.BigDecimal;
import java.util.Optional;

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
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.model.Link;
import swa.paymybuddy.model.LinkId;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.LinkRepository;
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.LinkService;
import swa.paymybuddy.service.TransferService;

@SpringBootTest
public class TransferServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransferRepository transferRepository;
	
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
	private TransferService transferService;

	@BeforeEach
	public void setup() 
	{
		transferRepository.deleteAll();
		accountRepository.deleteAll();
		linkRepository.deleteAll();
		userRepository.deleteAll();
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
	}
	
	@Test
	public void givenAuthenticatedWithLinkAndBothAccounts_whenTransferInternal_thenTransferIsCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		User myUser = userRepository.save(new User(0, 0, myEmail, passwordCrypted));
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		String description = "test transfer " + myEmail;
		Link link = new Link(myFriendUser.getId(), myUser.getId());
		linkRepository.save(link);
		Account accountDebit = accountRepository.save(new Account(myUser.getId(), Account.TYPE_INTERNAL));
		assertNotNull(accountDebit);
		Account accountCredit = accountRepository.save(new Account(myFriendUser.getId(), Account.TYPE_INTERNAL));
		assertNotNull(accountCredit);
		SecurityContext securityContext = (SecurityContext) mvc
				.perform(formLogin("/login").user(myEmail).password(passwordClear))
				.andExpect(authenticated())
				.andReturn().getRequest().getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(securityContext);
		// WHEN
		Transfer transfer = transferService.transferInternal(myFriendUser.getId(), myUser.getId(), description);
		// THEN
		assertNotNull(transfer);
		assertEquals(myUser.getId(), transfer.getAccountDebit().getUser().getId());
		assertEquals(myFriendUser.getId(), transfer.getAccountCredit().getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountDebit().getType());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountCredit().getType());
		assertEquals(description, transfer.getDescription());
	}
	
	// TODO tests négatifs sans prérequis : connexion, link, account
	
}
