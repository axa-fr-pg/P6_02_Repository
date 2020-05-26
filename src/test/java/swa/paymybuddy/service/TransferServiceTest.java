package swa.paymybuddy.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import swa.paymybuddy.service.exception.TransferOutsideOfMyNetworkException;

@SpringBootTest
public class TransferServiceTest {

	private User u1 = new User(1, 11, "email_1", "password_1"); // my id
	private User u2 = new User(2, 12, "email_2", "password_2"); // my friend's id
	private Relation r21 = new Relation(2, 1); // I can credit my friend
	private Account accountInt1 = new Account(u1, Account.TYPE_INTERNAL, new BigDecimal(11111.1), "bic_int_1", "iban_int_1");
	private Account accountInt2 = new Account(u2, Account.TYPE_INTERNAL, new BigDecimal(22.22), "bic_int_2", "iban_int_2");
	private Account accountExt1 = new Account(u1, Account.TYPE_EXTERNAL, new BigDecimal(333.22), "bic_ext_1", "iban_ext_1");
	private BigDecimal amount = new BigDecimal(200.00);
	private Transfer tInt21 = new Transfer(u2.getId(), u1.getId(), 0, "internal transfer from user 2 to user 1", amount); 
	private Transfer tFromOutside1 = new Transfer(accountInt1, accountExt1, null, 0, "transfer from outside user 1", amount); 
	private Transfer tToOutside1 = new Transfer(accountExt1, accountInt1, null, 0, "transfer to outside user 1", amount);
	private List<Transfer> tList = Arrays.asList(tInt21, tFromOutside1, tToOutside1);

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
		when(transferRepository.save(eq(tInt21))).thenReturn(tInt21);
		// WHEN
		Transfer t = transferService.transferInternal(tInt21);
		// THEN
		assertNotNull(t);
		assertEquals(u1.getId(), t.getAccountDebit().getUserId().getId());
		assertEquals(Account.TYPE_INTERNAL, t.getAccountDebit().getType());
		assertEquals(u2.getId(), t.getAccountCredit().getUserId().getId());
		assertEquals(Account.TYPE_INTERNAL, t.getAccountCredit().getType());
		assertEquals(tInt21.getTransferId(), t.getTransferId());
		assertEquals(tInt21.getDescription(), t.getDescription());
		assertEquals(tInt21.getAmount(), t.getAmount());	
	}

	
	@Test
	void givenFriendWithoutRelation_transferInternal_throwsException() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		// WHEN & THEN
		assertThrows(TransferOutsideOfMyNetworkException.class, () ->
			transferService.transferInternal(tInt21)
		);
	}

	@Test
	void givenExternalAccount_transferFromOutside_createsTransfer() throws Exception
	{
		// GIVEN
		BigDecimal commission = amount.divide(new BigDecimal(200.0));
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		when(accountService.operateTransfer(any(Account.class), eq(amount), eq(false))).thenReturn(accountExt1);
		when(accountService.operateTransfer(any(Account.class), eq(amount), eq(true))).thenReturn(accountInt1);
		when(accountService.operateTransfer(any(Account.class), eq(commission), eq(false))).thenReturn(accountInt2);
		when(transferRepository.save(any(Transfer.class))).thenReturn(tFromOutside1);
		// WHEN
		Transfer t = transferService.transferFromOutside(tFromOutside1); 
		// THEN
		assertNotNull(t);
		assertEquals(u1.getId(), t.getAccountDebit().getUserId().getId());
		assertEquals(Account.TYPE_EXTERNAL, t.getAccountDebit().getType());
		assertEquals(u1.getId(), t.getAccountCredit().getUserId().getId());
		assertEquals(Account.TYPE_INTERNAL, t.getAccountCredit().getType());
		assertEquals(tFromOutside1.getTransferId(), t.getTransferId());
		assertEquals(tFromOutside1.getDescription(), t.getDescription());
		assertEquals(tFromOutside1.getAmount(), t.getAmount());	
	}
	
	@Test
	void givenExternalAccount_transferToOutside_createsTransfer() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		when(accountService.operateTransfer(any(Account.class), eq(amount), eq(false))).thenReturn(accountInt1);
		when(accountService.operateTransfer(any(Account.class), eq(amount), eq(true))).thenReturn(accountExt1);
		when(transferRepository.save(eq(tToOutside1))).thenReturn(tToOutside1);
		// WHEN
		Transfer t = transferService.transferToOutside(tToOutside1);
		// THEN
		assertNotNull(t);
		assertEquals(u1.getId(), t.getAccountDebit().getUserId().getId());
		assertEquals(Account.TYPE_INTERNAL, t.getAccountDebit().getType());
		assertEquals(u1.getId(), t.getAccountCredit().getUserId().getId());
		assertEquals(Account.TYPE_EXTERNAL, t.getAccountCredit().getType());
		assertEquals(tToOutside1.getTransferId(), t.getTransferId());
		assertEquals(tToOutside1.getDescription(), t.getDescription());
		assertEquals(tToOutside1.getAmount(), t.getAmount());	
	}
	
	@Test
	void givenTransfers_getMyTransferList_returnsCorrectElements() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		when(transferRepository.findAll()).thenReturn(tList);
		// WHEN
		ArrayList<Transfer> myTransferList= transferService.getMyTransferList();
		// THEN
		assertNotNull(myTransferList);
		assertEquals(3, myTransferList.size());
	}
}
