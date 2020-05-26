package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.RelationId;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.service.exception.IllegalOperationOnMyOwnUserException;
import swa.paymybuddy.service.exception.NoAuthenticatedUserException;
import swa.paymybuddy.service.exception.RelationAlreadyExistsException;

@Service
@Transactional(rollbackFor = Exception.class)
public class RelationServiceImpl implements RelationService 
{
    private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

    @Autowired
	private RelationRepository relationRepository;

	@Autowired
	private UserService userService;

	private int getMyUserIdAndCheckMyFriendId(int myFriendUserId) throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException
	{
		logger.info("getMyUserIdAndCheckMyFriendId " + myFriendUserId);
		int myUserId = userService.getAuthenticatedUser().getId();
		if (myUserId == myFriendUserId) throw new IllegalOperationOnMyOwnUserException();		
		return myUserId;
	}
	
	protected Relation addUserToMyNetworkForCredit(int myFriendUserId) // I can credit my friend
			throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException, RelationAlreadyExistsException 
	{
		logger.info("addUserToMyNetworkForCredit " + myFriendUserId);
		int myUserId = getMyUserIdAndCheckMyFriendId(myFriendUserId);
		if (relationRepository.findById(new RelationId(myFriendUserId, myUserId)).isPresent())
			throw new RelationAlreadyExistsException();
		Relation relation = new Relation(myFriendUserId, myUserId);
		return relationRepository.save(relation);
	}

	protected Relation addUserToMyNetworkForDebit(int myFriendUserId) // My friend can credit me
			throws NoAuthenticatedUserException, IllegalOperationOnMyOwnUserException, RelationAlreadyExistsException 
	{
		logger.info("addUserToMyNetworkForDebit " + myFriendUserId);
		int myUserId = getMyUserIdAndCheckMyFriendId(myFriendUserId);
		if (relationRepository.findById(new RelationId(myUserId, myFriendUserId)).isPresent())
			throw new RelationAlreadyExistsException();
		Relation relation = new Relation(myUserId, myFriendUserId);
		return relationRepository.save(relation);
	}

	@Override // Add to my network for both credit and debit
	public boolean addUserToMyNetwork(int userId) throws NoAuthenticatedUserException, 
			IllegalOperationOnMyOwnUserException, RelationAlreadyExistsException 
	{
		logger.info("addUserToMyNetwork " + userId);
		return (addUserToMyNetworkForCredit(userId) != null && addUserToMyNetworkForDebit(userId) != null);
	}
}
