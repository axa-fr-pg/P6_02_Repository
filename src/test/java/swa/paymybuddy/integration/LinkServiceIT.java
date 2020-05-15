package swa.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import swa.paymybuddy.model.LinkId;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.LinkRepository;
import swa.paymybuddy.repository.UserRepository;
import swa.paymybuddy.service.LinkService;
import swa.paymybuddy.service.UserService;

@SpringBootTest
public class LinkServiceIT {

	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private LinkRepository linkRepository;

	@Autowired
    private UserRepository userRepository;

	private MockMvc mvc;

	@Autowired
	private UserService userService;

	@Autowired
	private LinkService linkService;

	@BeforeEach
	public void setup() 
	{
		linkRepository.deleteAll();
	}
	
	public void givenFriendUser_whenAddToMyNetwork_thenTwoNewLinksCreated() throws Exception
	{
		// GIVEN
		String myEmail = "email_1";
		userRepository.save(new User(0, 0, myEmail, passwordCrypted));
		mvc.perform(formLogin("/login").user(myEmail).password(passwordClear));
		int myUserId = userService.getAuthenticatedUser().getId();
		int myFriendId = 12345;
		// WHEN
		boolean result = linkService.addUserToMyNetwork(myFriendId);
		// THEN
		assertEquals(true, result);
		assertFalse(linkRepository.findById(new LinkId(myUserId, myFriendId)).isEmpty());
		assertFalse(linkRepository.findById(new LinkId(myFriendId, myUserId)).isEmpty());
	}
}
