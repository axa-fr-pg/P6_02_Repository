package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.service.exception.InvalidTransferAmountException;
import swa.paymybuddy.service.exception.TransferAmountGreaterThanAccountBalanceException;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountRepository accountRepository;
	
	public BigDecimal calculateBalanceAfterTransfer(BigDecimal initialBalance, BigDecimal amountToOperate, boolean credit) 
			throws TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException
	{
		logger.info("calculateBalance " + initialBalance.doubleValue() + " " + amountToOperate.doubleValue() + " " + credit);
		if (amountToOperate.doubleValue() <= 0) throw new InvalidTransferAmountException();
		BigDecimal amount = amountToOperate;
		if (!credit) amount = amount.negate();
		BigDecimal amountUpdated = initialBalance.add(amount);
		if (amountUpdated.doubleValue() < 0) throw new TransferAmountGreaterThanAccountBalanceException();
		return amountUpdated;
	}

	@Override
	public Account operateTransfer(Account account, BigDecimal amount, boolean credit) 
			throws TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException 
	{
		logger.info("operateTransfer " + account.getUserId().getId() + " " + account.getType() + " " + amount.doubleValue() + " " + credit);
		Optional<Account> accountInitial = accountRepository.findById(new AccountId(account.getUserId().getId(), account.getType()));
		if (accountInitial.isEmpty()) return null;
		logger.info("operateTransfer found account ");
		if (account.getType() == Account.TYPE_INTERNAL)
		{	// manage balance only for internal accounts
			account.setBalance(calculateBalanceAfterTransfer(accountInitial.get().getBalance(), amount, credit));
		}
		else account.setBalance(accountInitial.get().getBalance());
		logger.info("new balance to be saved " + account.getBalance());
		return accountRepository.save(account);
	}
}
