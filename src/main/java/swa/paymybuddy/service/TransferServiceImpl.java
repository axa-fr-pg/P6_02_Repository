package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.RelationId;
import swa.paymybuddy.model.Transfer;
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
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Override
	public Transfer transferInternal(Transfer transfer) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException 
	{
		int myUserId = userService.getAuthenticatedUser().getId();
		int myFriendId = transfer.getAccountCredit().getUserId().getId();
		BigDecimal amount = transfer.getAmount();
		logger.info("transferInternal to " +  myFriendId + " of " + amount.doubleValue() + " " + transfer.getDescription());
		Optional<Relation> relation = relationRepository.findById(new RelationId(myFriendId, myUserId));
		if(relation.isEmpty()) throw new TransferOutsideOfMyNetworkException();
//		transfer.setRelation(relation.get());
		transfer.setAccountCredit(accountService.operateTransfer(new Account(myFriendId, Account.TYPE_INTERNAL), amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(new Account(myUserId, Account.TYPE_INTERNAL), amount, false));
		return transferRepository.save(transfer);
	}
}
