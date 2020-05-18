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

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Override
	public Transfer transferInternal(int myFriendId, String description, BigDecimal amount) 
			throws TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException 
	{
		logger.info("transferInternal to " + myFriendId + " of " + amount.doubleValue() + " " + description);
		int myUserId = userService.getAuthenticatedUser().getId();
		Optional<Link> link = linkRepository.findById(new LinkId(myFriendId, myUserId));
		if(link.isEmpty()) throw new TransferOutsideOfMyNetworkException();
		accountService.operateTransfer(new AccountId(myUserId, Account.TYPE_INTERNAL), amount.negate(), false);
		accountService.operateTransfer(new AccountId(myFriendId, Account.TYPE_INTERNAL), amount, true);
		Optional<Account> accountCredit = accountRepository.findById(new AccountId(myFriendId, Account.TYPE_INTERNAL));
		if(accountCredit.isEmpty()) return null;
		Optional<Account> accountDebit = accountRepository.findById(new AccountId(myUserId, Account.TYPE_INTERNAL));
		if(accountDebit.isEmpty()) return null;
		Transfer transfer = new Transfer(accountCredit.get(), accountDebit.get(), link.get(), 0, description, amount);
		return transferRepository.save(transfer);
	}
}
