package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Relation;
import swa.paymybuddy.repository.RelationRepository;

@Service
public class RelationServiceImpl implements RelationService 
{
    private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

    @Autowired
	private RelationRepository relationRepository;

	@Autowired
	private UserService userService;

	private int getMyUserId(int myFriendUserId) throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException
	{
		logger.info("checkAuthenticatedUser " + myFriendUserId);
		int myUserId = userService.getAuthenticatedUser().getId();
		if (myUserId == myFriendUserId) throw new IllegalOperationOnMyOwnUserException();		
		return myUserId;
	}
	
	@Override
	public Relation addUserToMyNetworkForCredit(int myFriendUserId) // I can credit my friend
			throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException 
	{
		logger.info("addUserToMyNetworkForCredit " + myFriendUserId);		
		Relation relation = new Relation(myFriendUserId, getMyUserId(myFriendUserId));
		return relationRepository.save(relation);
	}

	@Override
	public Relation addUserToMyNetworkForDebit(int myFriendUserId) // My friend can credit me
			throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException 
	{
		logger.info("addUserToMyNetworkForDebit " + myFriendUserId);
		Relation relation = new Relation(getMyUserId(myFriendUserId), myFriendUserId);
		return relationRepository.save(relation);
	}

	@Override // Add to my network means enable transfers in both directions
	public boolean addUserToMyNetwork(int userId) throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException {
		logger.info("addUserToMyNetwork " + userId);
		return (addUserToMyNetworkForCredit(userId) != null && addUserToMyNetworkForDebit(userId) != null);
	}
}
