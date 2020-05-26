package swa.paymybuddy.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.service.exception.IllegalOperationOnMyOwnUserException;

@SpringBootTest
public class RelationServiceTest 
{
	private User u1 = new User(1, 11, "email_1", "password_1"); // my id
	private User u2 = new User(2, 12, "email_2", "password_2"); // my friend's id
	private Relation r12 = new Relation(1, 2); // my friend can credit me
	private Relation r21 = new Relation(2, 1); // I can credit my friend
	
	@MockBean
	private RelationRepository relationRepository;

	@Autowired // Autowiring the implementation instead of the interface to test protected methods
	private RelationServiceImpl relationService; 
	
	@MockBean
	UserService userService;

	@Test
	public void givenFriend_addUserToMyNetworkForCredit_returnsTheRightRelation() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		when(relationRepository.save(any(Relation.class))).thenReturn(r21);
		// WHEN
		Relation r = relationService.addUserToMyNetworkForCredit(u2.getId());
		// THEN
		assertNotNull(r);
		assertEquals(u2.getId(), r.getUserCredit().getId());
		assertEquals(u1.getId(), r.getUserDebit().getId());
	}

	@Test
	public void givenFriend_addUserToMyNetworkForDebit_returnsTheRightRelation() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		when(relationRepository.save(any(Relation.class))).thenReturn(r12);
		// WHEN
		Relation r = relationService.addUserToMyNetworkForCredit(u2.getId());
		// THEN
		assertNotNull(r);
		assertEquals(u1.getId(), r.getUserCredit().getId());
		assertEquals(u2.getId(), r.getUserDebit().getId());
	}	
	
	@Test
	public void givenUser_addMyselfToMyNetworkForDebit_throwsException() throws Exception
	{
		// GIVEN
		when(userService.getAuthenticatedUser()).thenReturn(u1);
		// WHEN & THEN
		assertThrows(IllegalOperationOnMyOwnUserException.class, () ->
				relationService.addUserToMyNetworkForCredit(u1.getId())
		);
	}	
}
