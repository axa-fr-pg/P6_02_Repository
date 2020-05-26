package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.RelationId;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.service.exception.InvalidTransferAmountException;
import swa.paymybuddy.service.exception.NoAuthenticatedUserException;
import swa.paymybuddy.service.exception.TransferAmountGreaterThanAccountBalanceException;
import swa.paymybuddy.service.exception.TransferOutsideOfMyNetworkException;

@Service
@Transactional(rollbackFor = Exception.class)
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
		transfer.setAccountCredit(accountService.operateTransfer(new Account(myFriendId, Account.TYPE_INTERNAL), amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(new Account(myUserId, Account.TYPE_INTERNAL), amount, false));
		return transferRepository.save(transfer);
	}
	
	@Override
	public Transfer transferFromOutside(Transfer transfer) 
			throws 	InvalidTransferAmountException, NoAuthenticatedUserException, TransferAmountGreaterThanAccountBalanceException 
	{
		int myUserId = userService.getAuthenticatedUser().getId();
		BigDecimal amount = transfer.getAmount();
		logger.info("transferFromOutside of " + amount.doubleValue() + " " + transfer.getDescription());
		transfer.setAccountCredit(accountService.operateTransfer(new Account(myUserId, Account.TYPE_INTERNAL), amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(new Account(myUserId, Account.TYPE_EXTERNAL), amount, false));
		return transferRepository.save(transfer);
	}
	
	@Override
	public Transfer transferToOutside(Transfer transfer) 
			throws 	InvalidTransferAmountException, NoAuthenticatedUserException, TransferAmountGreaterThanAccountBalanceException 
	{
		int myUserId = userService.getAuthenticatedUser().getId();
		BigDecimal amount = transfer.getAmount();
		logger.info("transferToOutside of " + amount.doubleValue() + " " + transfer.getDescription());
		transfer.setAccountCredit(accountService.operateTransfer(new Account(myUserId, Account.TYPE_EXTERNAL), amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(new Account(myUserId, Account.TYPE_INTERNAL), amount, false));
		return transferRepository.save(transfer);
	}
}
