package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account addInternal(int userId) 
	{
		logger.info("addInternal " + userId);
		Account account = new Account(userId, Account.TYPE_INTERNAL);
		return accountRepository.save(account);
	}

}
