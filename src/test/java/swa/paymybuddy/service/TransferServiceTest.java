package swa.paymybuddy.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.RelationId;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.TransferRepository;

@SpringBootTest
public class TransferServiceTest {

	private User u1 = new User(1, 11, "email_1", "password_1"); // my id
	private User u2 = new User(2, 12, "email_2", "password_2"); // my friend's id
	private Relation r21 = new Relation(2, 1); // I can credit my friend
	private Account accountInt1 = new Account(u1, Account.TYPE_INTERNAL, new BigDecimal(11111.1), "bic_1", "iban_1");
	private Account accountInt2 = new Account(u2, Account.TYPE_INTERNAL, new BigDecimal(22.22), "bic_2", "iban_2");
	private BigDecimal amount = new BigDecimal(123.45);
	private Transfer t12 = new Transfer(accountInt2, accountInt1, r21, 0, "description 12", amount); 

	@Autowired
	TransferService transferService;
	
	@MockBean
	private UserService userService;

	@MockBean
	private AccountService accountService;

	@MockBean
	private RelationRepository relationRepository;
	
	@MockBean
	private TransferRepository transferRepository;

	@Test
	void givenFriend_transferInternal_createsTransfer() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		when(relationRepository.findById(any(RelationId.class))).thenReturn(Optional.of(r21));
		when(accountService.operateTransfer(any(Account.class), eq(amount), eq(false))).thenReturn(accountInt1);
		when(accountService.operateTransfer(any(Account.class), eq(amount), eq(true))).thenReturn(accountInt2);
		when(transferRepository.save(eq(t12))).thenReturn(t12);
		// WHEN
		Transfer t = transferService.transferInternal(t12);
		// THEN
		assertNotNull(t);
		assertEquals(u1.getId(), t.getAccountDebit().getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, t.getAccountDebit().getType());
		assertEquals(u2.getId(), t.getAccountCredit().getUser().getId());
		assertEquals(Account.TYPE_INTERNAL, t.getAccountCredit().getType());
		assertEquals(t12.getTransferId(), t.getTransferId());
		assertEquals(t12.getDescription(), t.getDescription());
		assertEquals(t12.getAmount(), t.getAmount());	
	}
	
	@Test
	void givenFriendWithoutRelation_transferInternal_throwsException() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		// WHEN & THEN
		assertThrows(TransferOutsideOfMyNetworkException.class, () ->
			transferService.transferInternal(t12)
		);
	}
}
