package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.RelationRepository;

@Service
public class RelationServiceImpl implements RelationService 
{
    private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

    @Autowired
	private RelationRepository relationRepository;

	@Autowired
	private UserService userService;

	@Override
	public Relation addUserToMyNetworkForCredit(int myFriendUserId)
	{
		logger.info("addUserToMyNetworkForCredit " + myFriendUserId);
		User myUser = userService.getAuthenticatedUser();
		Relation relation = new Relation(myFriendUserId, myUser.getId());
		return relationRepository.save(relation);
	}

	@Override
	public Relation addUserToMyNetworkForDebit(int myFriendUserId)
	{
		logger.info("addUserToMyNetworkForDebit " + myFriendUserId);
		User myUser = userService.getAuthenticatedUser();
		Relation relation = new Relation(myUser.getId(), myFriendUserId);
		return relationRepository.save(relation);
	}

	@Override // Add to my network means enable transfers in both directions
	public boolean addUserToMyNetwork(int userId) {
		logger.info("addUserToMyNetwork " + userId);
		return (addUserToMyNetworkForCredit(userId) != null && addUserToMyNetworkForDebit(userId) != null);
	}
}
