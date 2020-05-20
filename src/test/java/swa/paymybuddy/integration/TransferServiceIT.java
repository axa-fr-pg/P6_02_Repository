package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.RelationService;
import swa.paymybuddy.service.TransferOutsideOfMyNetworkException;
import swa.paymybuddy.service.TransferService;

@SpringBootTest
public class TransferServiceIT {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RelationRepository relationRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private TestService testService;

	@Autowired
	private TransferService transferService;
	@Autowired
	private WebApplicationContext context;
	
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

	private MockMvc mvc;
	
	@BeforeEach
	public void setup() 
	{
		testService.cleanAllTables();
		mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
	}

	@Test
	public void givenAuthenticatedWithrelationAndBothAccounts_whenTransferInternal_thenTransferIsCreatedAndBalanceUpdated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		String description = "test transfer " + myEmail;
		Relation relation = new Relation(myFriendUser.getId(), myUser.getId());
		relationRepository.save(relation);
		BigDecimal amount = new BigDecimal(12345.67);
		BigDecimal myBalance = new BigDecimal(111111.22);
		BigDecimal myFriendBalance = new BigDecimal(33333.44);
		Account accountDebit = accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, myBalance, "", ""));
		assertNotNull(accountDebit);
		Account accountCredit = accountRepository.save(new Account(myFriendUser, Account.TYPE_INTERNAL, myFriendBalance, "", ""));
		assertNotNull(accountCredit);
		// WHEN
		Transfer transfer = transferService.transferInternal(myFriendUser.getId(), description, amount);
		accountCredit = accountRepository.findById(new AccountId(myFriendUser.getId(), Account.TYPE_INTERNAL)).get();
		accountDebit = accountRepository.findById(new AccountId(myUser.getId(), Account.TYPE_INTERNAL)).get();
		// THEN
		assertNotNull(transfer);
		assertEquals(myUser.getId(), transfer.getAccountDebit().getUser().getId());
		assertEquals(myFriendUser.getId(), transfer.getAccountCredit().getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountDebit().getType());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountCredit().getType());
		assertEquals(description, transfer.getDescription());
		assertEquals(amount, transfer.getAmount());
		assertEquals(myBalance.doubleValue(), accountDebit.getBalance().doubleValue() + amount.doubleValue(), 0.0000000000001);
		assertEquals(myFriendBalance.doubleValue(), accountCredit.getBalance().doubleValue() - amount.doubleValue(), 0.0000000000001);
	}
	
	@Test
	public void givenAuthenticatedWithoutrelation_whenTransferInternal_thenTransferIsNotCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		String description = "test transfer " + myEmail;
		Account accountDebit = accountRepository.save(new Account(myUser.getId(), Account.TYPE_INTERNAL));
		assertNotNull(accountDebit);
		Account accountCredit = accountRepository.save(new Account(myFriendUser.getId(), Account.TYPE_INTERNAL));
		assertNotNull(accountCredit);
		BigDecimal amount = new BigDecimal(12345.67);
		// WHEN & THEN
	    assertThrows(TransferOutsideOfMyNetworkException.class, () -> 
	    	transferService.transferInternal(myFriendUser.getId(), description, amount)
	    );
	}
	
	@Test
	public void givenAuthenticatedWithoutAccount_whenTransferInternal_thenTransferIsNotCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		String description = "test transfer " + myEmail;
		Relation relation = new Relation(myFriendUser.getId(), myUser.getId());
		relationRepository.save(relation);
		BigDecimal amount = new BigDecimal(12345.67);
		// WHEN & THEN
	    assertThrows(JpaSystemException.class, () -> 
	    	transferService.transferInternal(myFriendUser.getId(), description, amount)
	    );
	}
}
