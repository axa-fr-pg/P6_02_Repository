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
	public void givenExistingAccount_operateInternalCreditTransfer_updatesBalance() throws Exception
	{
		// GIVEN
		String myFriendEmail = "email_1";
		BigDecimal balanceInitial = new BigDecimal(12345.67);
		BigDecimal amountCredit = new BigDecimal(111.22);
		User myFriend = userRepository.save(new User(0, 0, myFriendEmail, "not required"));
		Account myFriendAccount = accountRepository.save(new Account(myFriend, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		// WHEN
		Account account = accountService.operateTransfer(myFriendAccount, amountCredit, true);
		// THEN
		assertNotNull(account);
		assertEquals(myFriend.getId(), account.getUserId().getId());
		assertEquals(Account.TYPE_INTERNAL, account.getType());
		assertEquals(balanceInitial.doubleValue(), account.getBalance().doubleValue() - amountCredit.doubleValue(), 0.0000000000001);
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}
	
	@Test
	public void givenSufficientMoney_operateInternalDebitTransfer_updatesBalance() throws Exception
	{
		// GIVEN
		String myFriendEmail = "email_2";
		User myFriend = userRepository.save(new User(0, 0, myFriendEmail, "not required"));
		BigDecimal balanceInitial = new BigDecimal(12345.67);
		Account myFriendAccount = accountRepository.save(new Account(myFriend, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		BigDecimal amountCredit = new BigDecimal(111.22);
		// WHEN
		Account account = accountService.operateTransfer(myFriendAccount, amountCredit, false);
		// THEN
		assertNotNull(account);
		assertEquals(myFriend.getId(), account.getUserId().getId());
		assertEquals(Account.TYPE_INTERNAL, account.getType());
		assertEquals(balanceInitial.doubleValue(), account.getBalance().doubleValue() + amountCredit.doubleValue(), 0.0000000000001);
		assertEquals("", account.getBic());
		assertEquals("", account.getIban());
	}
	
	@Test
	public void givenBalanceBelowTransferAmount_operateInternalDebitTransfer_throwsException() throws Exception
	{
		// GIVEN
		String myFriendEmail = "email_3";
		User myFriend = userRepository.save(new User(0, 0, myFriendEmail, "not required"));
		BigDecimal balanceInitial = new BigDecimal(12.67);
		Account myFriendAccount = accountRepository.save(new Account(myFriend, Account.TYPE_INTERNAL, balanceInitial, "", "") );
		BigDecimal amountCredit = new BigDecimal(111.22);
		// WHEN & THEN
		assertThrows( TransferAmountGreaterThanAccountBalanceException.class, () ->
			accountService.operateTransfer(myFriendAccount, amountCredit, false)
		);
	}
}
