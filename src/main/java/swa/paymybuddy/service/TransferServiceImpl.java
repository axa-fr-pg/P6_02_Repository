package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.model.Link;
import swa.paymybuddy.model.LinkId;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.LinkRepository;
import swa.paymybuddy.repository.TransferRepository;

@Service
public class TransferServiceImpl implements TransferService{

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
	private TransferRepository transferRepository;

    @Autowired
	private LinkRepository linkRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Transfer transferInternal(int userCreditId, int userDebitId, String description, BigDecimal amount) 
	{
		logger.info("transferInternal " + userCreditId + " - " + userDebitId + " " + description);
		Optional<Link> link = linkRepository.findById(new LinkId(userCreditId, userDebitId));
		if(link.isEmpty()) return null;
		Optional<Account> accountCredit = accountRepository.findById(new AccountId(userCreditId, Account.TYPE_INTERNAL));
		if(accountCredit.isEmpty()) return null;
		Optional<Account> accountDebit = accountRepository.findById(new AccountId(userDebitId, Account.TYPE_INTERNAL));
		if(accountDebit.isEmpty()) return null;
		Transfer transfer = new Transfer(accountCredit.get(), accountDebit.get(), link.get(), 0, description, amount);
		return transferRepository.save(transfer);
	}
}
