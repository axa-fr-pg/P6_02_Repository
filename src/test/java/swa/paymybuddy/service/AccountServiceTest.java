package swa.paymybuddy.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.service.exception.InvalidTransferAmountException;
import swa.paymybuddy.service.exception.TransferAmountGreaterThanAccountBalanceException;

@SpringBootTest
public class AccountServiceTest 
{
	private User u1 = new User(1, 11, "email_1", "password_1"); // my id
	private User u2 = new User(2, 12, "email_2", "password_2"); // my friend's id
	private Account accountInt1 = new Account(u1, Account.TYPE_INTERNAL, new BigDecimal(11111.1), "bic_1", "iban_1");
	private Account accountInt2 = new Account(u2, Account.TYPE_INTERNAL, new BigDecimal(22.22), "bic_2", "iban_2");
	private BigDecimal amount1 = new BigDecimal(123.45);
	private BigDecimal amount2 = new BigDecimal(-2345.678);
			
	@MockBean
	AccountRepository accountRepository;
	
	@Autowired
	AccountService accountService;

	@Test
	void givenBalanceAndAmount_calculateBalanceAfterCreditTransfer_returnsCorrectAmount() throws Exception
	{
		// GIVEN
		// WHEN
		BigDecimal newBalance = accountService.calculateBalanceAfterTransfer(accountInt1.getBalance(), amount1, true);
		// THEN
		assertEquals(accountInt1.getBalance().doubleValue(), newBalance.doubleValue() - amount1.doubleValue(), 0.0000000000001);
	}
	
	@Test
	void givenBalanceAndAmount_calculateBalanceAfterDebitTransfer_returnsCorrectAmount() throws Exception
	{
		// GIVEN
		// WHEN
		BigDecimal newBalance = accountService.calculateBalanceAfterTransfer(accountInt1.getBalance(), amount1, false);
		// THEN
		assertEquals(accountInt1.getBalance().doubleValue(), newBalance.doubleValue() + amount1.doubleValue(), 0.0000000000001);
	}
	
	@Test
	void givenInvalidTransferAmount_calculateBalanceAfterCreditTransfer_throwsException()
	{
		// GIVEN
		// WHEN & THEN
		assertThrows(InvalidTransferAmountException.class, () ->
			accountService.calculateBalanceAfterTransfer(accountInt1.getBalance(), amount2, true)
		);
	}
	
	@Test
	void givenInsufficientMoneyAvailable_calculateBalanceAfterDebitTransfer_throwsException()
	{
		// GIVEN
		// WHEN & THEN
		assertThrows(TransferAmountGreaterThanAccountBalanceException.class, () ->
			accountService.calculateBalanceAfterTransfer(accountInt2.getBalance(), amount1, false)
		);
	}
}
