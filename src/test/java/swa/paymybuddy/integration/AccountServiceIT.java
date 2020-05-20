package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.AccountService;
import swa.paymybuddy.service.TransferAmountGreaterThanAccountBalanceException;

@SpringBootTest
public class AccountServiceIT {

	@Autowired
    private AccountService accountService;

	@Autowired
    private AccountRepository accountRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private TestService testService;

	@Autowired
	private WebApplicationContext context;
	
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

	@BeforeEach
	public void setup() 
	{
		testService.cleanAllTables();
		MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
	}
	
	@Test
	public void givenExistingAccount_whenOperateCreditTransfer_thenBalanceIsUpdated() throws Exception
	{
		// GIVEN
		String myFriendEmail = "email_3";
		User myFriend = userRepository.save(new User(0, 0, myFriendEmail, "not required"));
		BigDecimal balanceInitial = new BigDecimal(12345.67);
		accountRepository.save(new Account(myFriend, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		BigDecimal amountCredit = new BigDecimal(111.22);
		// WHEN
		Account account = accountService.operateTransfer(new AccountId(myFriend.getId(), Account.TYPE_INTERNAL), amountCredit, true);
		// THEN
		assertNotNull(account);
		assertEquals(myFriend.getId(), account.getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, account.getType());
		assertEquals(balanceInitial.doubleValue(), account.getBalance().doubleValue() - amountCredit.doubleValue(), 0.0000000000001);
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}
	
	@Test
	public void givenExistingAccount_whenOperateInternalCreditTransfer_thenBalanceIsUpdated() throws Exception
	{
		// GIVEN
		String myFriendEmail = "email_4";
		User myFriend = userRepository.save(new User(0, 0, myFriendEmail, "not required"));
		BigDecimal balanceInitial = new BigDecimal(12345.67);
		accountRepository.save(new Account(myFriend, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		BigDecimal amountCredit = new BigDecimal(111.22);
		// WHEN
		Account account = accountService.operateTransfer(new AccountId(myFriend.getId(), Account.TYPE_INTERNAL), amountCredit, true);
		// THEN
		assertNotNull(account);
		assertEquals(myFriend.getId(), account.getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, account.getType());
		assertEquals(balanceInitial.doubleValue(), account.getBalance().doubleValue() - amountCredit.doubleValue(), 0.0000000000001);
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}
	
	@Test
	public void givenExistingAccountWithSufficientProvision_whenOperateInternalDebitTransfer_thenBalanceIsUpdated() throws Exception
	{
		// GIVEN
		String myEmail = "email_5";
		User myUser = userRepository.save(new User(0, 0, myEmail, "not required"));
		BigDecimal balanceInitial = new BigDecimal(54321.98);
		accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		BigDecimal amountDebit = new BigDecimal(-54321.97);
		// WHEN
		Account account = accountService.operateTransfer(new AccountId(myUser.getId(), Account.TYPE_INTERNAL), amountDebit, false);
		// THEN
		assertNotNull(account);
		assertEquals(myUser.getId(), account.getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, account.getType());
		assertEquals(balanceInitial.doubleValue(), account.getBalance().doubleValue() - amountDebit.doubleValue(), 0.0000000000001);
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}

	@Test
	public void givenExistingAccountWithInsufficientProvision_whenOperateInternalDebitTransfer_thenExceptionIsRaised() throws Exception
	{
		// GIVEN
		String myEmail = "email_5";
		User myUser = userRepository.save(new User(0, 0, myEmail, "not required"));
		BigDecimal balanceInitial = new BigDecimal(54321.98);
		accountRepository.save(new Account(myUser, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		BigDecimal amountDebit = new BigDecimal(-54321.99);
		// WHEN & THEN
	    assertThrows(TransferAmountGreaterThanAccountBalanceException.class, () -> 
	    	accountService.operateTransfer(new AccountId(myUser.getId(), Account.TYPE_INTERNAL), amountDebit, false)
	    );
	}
}
