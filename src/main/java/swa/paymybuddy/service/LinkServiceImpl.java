package swa.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import swa.paymybuddy.model.Link;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.LinkRepository;

public class LinkServiceImpl implements LinkService 
{
	@Autowired
	private LinkRepository linkRepository;

	@Autowired
	private UserService userService;

	@Override
	public Link addUserToMyNetworkForCredit(int myFriendUserId)
	{
		User myUser = userService.getAuthenticatedUser();
		Link link = new Link(myFriendUserId, myUser.getId());
		return linkRepository.save(link);
	}

	@Override
	public Link addUserToMyNetworkForDebit(int myFriendUserId)
	{
		User myUser = userService.getAuthenticatedUser();
		Link link = new Link(myUser.getId(), myFriendUserId);
		return linkRepository.save(link);
	}

	@Override // Initial assumption is to add user for both transfer directions
	public boolean addUserToMyNetwork(int userId) {
		return (addUserToMyNetworkForCredit(userId) != null && addUserToMyNetworkForDebit(userId) != null);
	}
}
