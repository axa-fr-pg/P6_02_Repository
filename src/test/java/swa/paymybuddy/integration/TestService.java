package swa.paymybuddy.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.PersistentLoginsRepository;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.repository.UserRepository;

@Service
public class TestService 
{
	private String passwordClear = "password";
	private String passwordCrypted = "$2y$10$Tbpujg3N8c91uCfOBMLw/eoEVfJp9hqV1.9qcbZZWxgYjuX1Zv9.G";

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransferRepository transferRepository;
	
	@Autowired
	private RelationRepository relationRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
    private PersistentLoginsRepository persistentLoginsRepository;

	public void cleanAllTables()
	{
		transferRepository.deleteAll();
		accountRepository.deleteAll();
		relationRepository.deleteAll();
		persistentLoginsRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	public User loginAndReturnUser(MockMvc mvc, String email) throws Exception
	{
		User myUser = userRepository.save(new User(0, 0, email, passwordCrypted));
		SecurityContext securityContext = (SecurityContext) mvc
				.perform(formLogin("/login").user(email).password(passwordClear))
				.andExpect(authenticated())
				.andReturn().getRequest().getSession()
				.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(securityContext);
		return myUser;
	}
}
