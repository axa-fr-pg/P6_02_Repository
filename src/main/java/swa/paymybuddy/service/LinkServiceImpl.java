package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Link;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.LinkRepository;

@Service
public class LinkServiceImpl implements LinkService 
{
    private static final Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);

    @Autowired
	private LinkRepository linkRepository;

	@Autowired
	private UserService userService;

	@Override
	public Link addUserToMyNetworkForCredit(int myFriendUserId)
	{
		logger.info("addUserToMyNetworkForCredit " + myFriendUserId);
		User myUser = userService.getAuthenticatedUser();
		Link link = new Link(myFriendUserId, myUser.getId());
		return linkRepository.save(link);
	}

	@Override
	public Link addUserToMyNetworkForDebit(int myFriendUserId)
	{
		logger.info("addUserToMyNetworkForDebit " + myFriendUserId);
		User myUser = userService.getAuthenticatedUser();
		Link link = new Link(myUser.getId(), myFriendUserId);
		return linkRepository.save(link);
	}

	@Override // Initial assumption is to add user for both transfer directions
	public boolean addUserToMyNetwork(int userId) {
		logger.info("addUserToMyNetwork " + userId);
		return (addUserToMyNetworkForCredit(userId) != null && addUserToMyNetworkForDebit(userId) != null);
	}
}
