package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.RelationId;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.TransferRepository;

@Service
public class TransferServiceImpl implements TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
	private TransferRepository transferRepository;

    @Autowired
	private RelationRepository relationRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Override
	public Transfer transferInternal(int myFriendId, String description, BigDecimal amount) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException 
	{
		logger.info("transferInternal to " + myFriendId + " of " + amount.doubleValue() + " " + description);
		int myUserId = userService.getAuthenticatedUser().getId();
		Optional<Relation> relation = relationRepository.findById(new RelationId(myFriendId, myUserId));
		if(relation.isEmpty()) throw new TransferOutsideOfMyNetworkException();
		Account accountDebit = accountService.operateTransfer(new AccountId(myUserId, Account.TYPE_INTERNAL), amount.negate(), false);
		Account accountCredit = accountService.operateTransfer(new AccountId(myFriendId, Account.TYPE_INTERNAL), amount, true);
		Transfer transfer = new Transfer(accountCredit, accountDebit, relation.get(), 0, description, amount);
		return transferRepository.save(transfer);
	}
}
