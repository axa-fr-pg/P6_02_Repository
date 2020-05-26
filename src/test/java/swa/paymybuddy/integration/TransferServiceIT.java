package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.TransferService;
import swa.paymybuddy.service.UserService;

@SpringBootTest
public class TransferServiceIT {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RelationRepository relationRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TestService testService;

	@Autowired
	private TransferService transferService;
	
	@Autowired
	private TransferRepository transferRepository;
	
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
		testService.createCommissionUserAndAccount();
	}

	@Test
	public void givenAllPrerequisites_whenTransferInternal_thenTransferIsCreatedAndBalancesAreUpdated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		String description = "test transfer " + myEmail;
		BigDecimal amount = new BigDecimal(2000.00);
		BigDecimal comAmount = new BigDecimal(amount.doubleValue() / 200.0);
		BigDecimal myBalance = new BigDecimal(111111.22);
		BigDecimal myFriendBalance = new BigDecimal(33333.44);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		Relation relation = new Relation(myFriendUser.getId(), myUser.getId());
		relationRepository.save(relation);
		Account accountDebit = accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, myBalance, "", ""));
		assertNotNull(accountDebit);
		Account accountCredit = accountRepository.save(new Account(myFriendUser, Account.TYPE_INTERNAL, myFriendBalance, "", ""));
		assertNotNull(accountCredit);
		Transfer transferRequest = new Transfer(myFriendUser.getId(), myUser.getId(), Account.TYPE_INTERNAL, description, amount);
		// WHEN
		Transfer transfer = transferService.transferInternal(transferRequest);
		accountCredit = accountRepository.findById(new AccountId(myFriendUser.getId(), Account.TYPE_INTERNAL)).get();
		accountDebit = accountRepository.findById(new AccountId(myUser.getId(), Account.TYPE_INTERNAL)).get();
		int transferCount = transferRepository.findAll().size();
		Account accountCom = accountRepository.findById(new AccountId(userService.getCommissionUserId(), Account.TYPE_INTERNAL)).get();
		// THEN
		assertNotNull(transfer);
		assertEquals(2, transferCount);
		assertEquals(myUser.getId(), transfer.getAccountDebit().getUserId().getId());
		assertEquals(myFriendUser.getId(), transfer.getAccountCredit().getUserId().getId());
		assertEquals(amount, transfer.getAmount());
		assertEquals(description, transfer.getDescription());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountDebit().getType());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountCredit().getType());
		assertEquals(myBalance.doubleValue(), accountDebit.getBalance().add(amount).add(comAmount).doubleValue(), 0.0000000000001);
		assertEquals(myFriendBalance.doubleValue(), accountCredit.getBalance().doubleValue() - amount.doubleValue(), 0.0000000000001);
		assertEquals(comAmount.doubleValue(), accountCom.getBalance().doubleValue(), 0.0000000000001);
	}
	
	@Test
	public void givenAuthenticatedWithoutAccount_whenTransferInternal_thenTransferIsNotCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_2";
		String description = "test transfer " + myEmail;
		BigDecimal amount = new BigDecimal(12345.67);
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		Relation relation = new Relation(myFriendUser.getId(), myUser.getId());
		relationRepository.save(relation);
		Transfer transferRequest = new Transfer(myFriendUser.getId(), myUser.getId(), Account.TYPE_INTERNAL, description, amount);
		// WHEN & THEN
	    assertThrows(JpaSystemException.class, () -> 
	    	transferService.transferInternal(transferRequest)
	    );
	}
	
	@Test
	public void givenAllPrerequisites_whenTransferFromOutside_thenTransferIsCreatedAndBalanceIsUpdated() throws Exception
	{
		// GIVEN
		String myEmail = "email_3";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		String description = "test transfer " + myEmail;
		BigDecimal amount = new BigDecimal(200000.000);
		BigDecimal comAmount = new BigDecimal(amount.doubleValue() / 200.0);
		BigDecimal myInternalBalance = new BigDecimal(111111.22);
		BigDecimal myExternalBalance = new BigDecimal(33333.44);
		assertNotNull(myUser);
		Account accountDebit = accountRepository.save(new Account(myUser, Account.TYPE_EXTERNAL, myExternalBalance, "", ""));
		assertNotNull(accountDebit);
		Account accountCredit = accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, myInternalBalance, "", ""));
		assertNotNull(accountCredit);
		Transfer transferRequest = new Transfer(accountCredit, accountDebit, null, 0, description, amount);
		// WHEN
		Transfer transfer = transferService.transferFromOutside(transferRequest);
		accountCredit = accountRepository.findById(new AccountId(myUser.getId(), Account.TYPE_INTERNAL)).get();
		Account accountCom = accountRepository.findById(new AccountId(userService.getCommissionUserId(), Account.TYPE_INTERNAL)).get();
		// THEN
		assertNotNull(transfer);
		assertEquals(myUser.getId(), transfer.getAccountDebit().getUserId().getId());
		assertEquals(myUser.getId(), transfer.getAccountCredit().getUserId().getId());
		assertEquals(amount, transfer.getAmount());
		assertEquals(description, transfer.getDescription());
		assertEquals(Account.TYPE_EXTERNAL, transfer.getAccountDebit().getType());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountCredit().getType());
		assertEquals(myInternalBalance.doubleValue(), 
				accountCredit.getBalance().subtract(amount).add(comAmount).doubleValue(), 0.0000000000001);
		assertEquals(comAmount.doubleValue(), accountCom.getBalance().doubleValue(), 0.0000000000001);
	}
	
	@Test
	public void givenAllPrerequisites_whenTransferToOutside_thenTransferIsCreatedAndBalanceIsUpdated() throws Exception
	{
		// GIVEN
		String myEmail = "email_4";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		String description = "test transfer " + myEmail;
		BigDecimal amount = new BigDecimal(20000.00);
		BigDecimal comAmount = new BigDecimal(amount.doubleValue() / 200.0);
		BigDecimal myInternalBalance = new BigDecimal(111111.22);
		BigDecimal myExternalBalance = new BigDecimal(33333.44);
		assertNotNull(myUser);
		Account accountDebit = accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, myInternalBalance, "", ""));
		assertNotNull(accountDebit);
		Account accountCredit = accountRepository.save(new Account(myUser, Account.TYPE_EXTERNAL, myExternalBalance, "", ""));
		assertNotNull(accountCredit);
		Transfer transferRequest = new Transfer(accountCredit, accountDebit, null, 0, description, amount);
		// WHEN
		Transfer transfer = transferService.transferToOutside(transferRequest);
		accountDebit = accountRepository.findById(new AccountId(myUser.getId(), Account.TYPE_INTERNAL)).get();
		Account accountCom = accountRepository.findById(new AccountId(userService.getCommissionUserId(), Account.TYPE_INTERNAL)).get();
		// THEN
		assertNotNull(transfer);
		assertEquals(myUser.getId(), transfer.getAccountDebit().getUserId().getId());
		assertEquals(myUser.getId(), transfer.getAccountCredit().getUserId().getId());
		assertEquals(amount, transfer.getAmount());
		assertEquals(description, transfer.getDescription());
		assertEquals(Account.TYPE_INTERNAL, transfer.getAccountDebit().getType());
		assertEquals(Account.TYPE_EXTERNAL, transfer.getAccountCredit().getType());
		assertEquals(myInternalBalance.doubleValue(), accountDebit.getBalance().add(amount).add(comAmount).doubleValue(), 0.0000000000001);
		assertEquals(comAmount.doubleValue(), accountCom.getBalance().doubleValue(), 0.0000000000001);
	}
	
	@Test
	public void givenMissingAccountForDebit_whenTransferInternal_thenTransactionIsRolledBack() throws Exception
	{
		// GIVEN
		String myEmail = "email_5";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		String description = "test transfer " + myEmail;
		BigDecimal amount = new BigDecimal(12345.67);
		BigDecimal myFriendBalance = new BigDecimal(33333.44);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		Relation relation = new Relation(myFriendUser.getId(), myUser.getId());
		relationRepository.save(relation);
		Account accountCredit = accountRepository.save(new Account(myFriendUser, Account.TYPE_INTERNAL, myFriendBalance, "", ""));
		assertNotNull(accountCredit);
		Transfer transferRequest = new Transfer(myFriendUser.getId(), myUser.getId(), Account.TYPE_INTERNAL, description, amount);
		// WHEN
		Transfer transfer = transferRequest;
		try { transfer = transferService.transferInternal(transferRequest); } catch (Exception e) {}
		accountCredit = accountRepository.findById(new AccountId(myFriendUser.getId(), Account.TYPE_INTERNAL)).get();
		// THEN
		assertNull(transfer.getAccountDebit());
		assertEquals(myFriendBalance.doubleValue(), accountCredit.getBalance().doubleValue(), 0.0000000000001);
	}
	
	@Test
	public void givenTransfersByMyFriendAndByMyself_getMyTransferList_returnsOnlyMyTransfers() throws Exception
	{
		// GIVEN
		String myEmail = "email_6";
		User myUser = testService.loginAndReturnUser(mvc, myEmail);
		BigDecimal amount = new BigDecimal(12345.67);
		BigDecimal myBalance = new BigDecimal(111111.22);
		BigDecimal myFriendBalance = new BigDecimal(33333.44);
		assertNotNull(myUser);
		User myFriendUser = userRepository.save(new User(0, 0, myEmail+"_friend", "not_used"));
		assertNotNull(myFriendUser);
		Relation relation = new Relation(myFriendUser.getId(), myUser.getId());
		relationRepository.save(relation);
		Account myAccountInternal = accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, myBalance, "", ""));
		assertNotNull(myAccountInternal);
		Account myAccountExternal = accountRepository.save(new Account(myUser, Account.TYPE_EXTERNAL, myBalance, "", ""));
		assertNotNull(myAccountExternal);
		Account myFriendAccount = accountRepository.save(new Account(myFriendUser, Account.TYPE_INTERNAL, myFriendBalance, "", ""));
		assertNotNull(myFriendAccount);
		String description1 = "test transfer " + myEmail + " 1";
		String description2 = "test transfer " + myEmail + " 2";
		String description3 = "test transfer " + myEmail + " 3";
		transferRepository.save(new Transfer(myFriendAccount, myAccountInternal, null, 0, description1, amount));
		transferRepository.save(new Transfer(myAccountInternal, myFriendAccount, null, 0, "this one belongs my friend", amount));
		transferRepository.save(new Transfer(myFriendAccount, myAccountExternal, null, 0, description2, amount));
		transferRepository.save(new Transfer(myFriendAccount, myAccountInternal, null, 0, description3, amount));
		// WHEN
		ArrayList<Transfer> myTransferList = transferService.getMyTransferList();
		// THEN
		assertNotNull(myTransferList);
		assertEquals(3, myTransferList.size());
		int counter = 0;
		for (Transfer t : myTransferList) {
			if (t.getDescription().equals(description1)) counter += 1;
			if (t.getDescription().equals(description2)) counter += 10;
			if (t.getDescription().equals(description3)) counter += 100;
		}
		assertEquals(111, counter);
	}
} 
