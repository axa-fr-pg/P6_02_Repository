package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account operateTransfer(AccountId accountId, BigDecimal amount, boolean credit) 
			throws TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException 
	{
		logger.info("operateTransfer " + accountId.getUser() + " " + accountId.getType() + " " + amount.doubleValue() + " " + credit);
		Optional<Account> accountInitial = accountRepository.findById(accountId);
		if (accountInitial.isEmpty()) return null;
		logger.info("operateTransfer found account ");
		if (amount.doubleValue() == 0) throw new InvalidTransferAmountException();
		if (credit && amount.doubleValue() < 0) throw new InvalidTransferAmountException();
		logger.info("operateTransfer has correct amount");
		Account accountUpdated = accountInitial.get();
		BigDecimal amountUpdated = accountUpdated.getBalance().add(amount);
		if (amountUpdated.doubleValue() < 0) throw new TransferAmountGreaterThanAccountBalanceException();
		accountUpdated.setBalance(amountUpdated);
		logger.info("new balance to be saved " + accountUpdated.getBalance());
		return accountRepository.save(accountUpdated);
	}
}
