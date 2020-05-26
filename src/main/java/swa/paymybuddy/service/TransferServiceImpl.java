package swa.paymybuddy.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import swa.paymybuddy.service.exception.NoCommissionUserException;
import swa.paymybuddy.service.exception.TransferAmountGreaterThanAccountBalanceException;
import swa.paymybuddy.service.exception.TransferOutsideOfMyNetworkException;

@Service
@Transactional(rollbackFor = Exception.class) // Roll back if any exception is raised
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

	private BigDecimal calculateCommission(BigDecimal transferAmount) {
		return new BigDecimal(transferAmount.doubleValue() / 200.0); // 0,5 %
	}
	
	@Override
	public Transfer transferInternal(Transfer transfer) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException, NoCommissionUserException 
	{
		int myUserId = userService.getAuthenticatedUser().getId();
		int myFriendId = transfer.getAccountCredit().getUserId().getId();
		BigDecimal amount = transfer.getAmount();
		logger.info("transferInternal to " +  myFriendId + " of " + amount.doubleValue() + " " + transfer.getDescription());
		Optional<Relation> relation = relationRepository.findById(new RelationId(myFriendId, myUserId));
		if(relation.isEmpty()) throw new TransferOutsideOfMyNetworkException();
		// Transfer the commission first
		Account myAccount = new Account(myUserId, Account.TYPE_INTERNAL);
		Account myFriendAccount = new Account(myFriendId, Account.TYPE_INTERNAL);
		Account comAccount = new Account(userService.getCommissionUserId(), Account.TYPE_INTERNAL);
		BigDecimal comAmount = calculateCommission(amount);
		transfer.setAccountCredit(accountService.operateTransfer(comAccount, comAmount, true));
		transfer.setAccountDebit(accountService.operateTransfer(myAccount, comAmount, false));
		Transfer comTransfer = transferRepository.save(transfer);
		if (comTransfer == null) return null;
		// Operate the transfer then
		transfer.setAccountCredit(accountService.operateTransfer(myFriendAccount, amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(myAccount, amount, false));
		return transferRepository.save(transfer);
	}
	
	@Override
	public Transfer transferFromOutside(Transfer transfer) throws 	InvalidTransferAmountException, NoAuthenticatedUserException,
					TransferAmountGreaterThanAccountBalanceException, NoCommissionUserException 
	{
		int myUserId = userService.getAuthenticatedUser().getId();
		BigDecimal amount = transfer.getAmount();
		logger.info("transferFromOutside of " + amount.doubleValue() + " " + transfer.getDescription());
		// Operate the transfer first
		Account myInternalAccount = new Account(myUserId, Account.TYPE_INTERNAL);
		Account myExternalAccount = new Account(myUserId, Account.TYPE_EXTERNAL);
		Account comAccount = new Account(userService.getCommissionUserId(), Account.TYPE_INTERNAL);
		transfer.setAccountCredit(accountService.operateTransfer(myInternalAccount, amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(myExternalAccount, amount, false));
		transfer = transferRepository.save(transfer);
		if (transfer == null) return null;
		// Transfer the commission then
		BigDecimal comAmount = calculateCommission(amount);
		Transfer comTransfer = new Transfer(
				accountService.operateTransfer(comAccount, comAmount, true), 
				accountService.operateTransfer(myInternalAccount, comAmount, false),
				null, 0, transfer.getDescription(), comAmount);
		comTransfer = transferRepository.save(comTransfer);
		if (comTransfer == null) return null;
		return transfer;
	}
	
	@Override
	public Transfer transferToOutside(Transfer transfer) throws InvalidTransferAmountException, NoAuthenticatedUserException,
			TransferAmountGreaterThanAccountBalanceException, NoCommissionUserException 
	{
		int myUserId = userService.getAuthenticatedUser().getId();
		BigDecimal amount = transfer.getAmount();
		logger.info("transferToOutside of " + amount.doubleValue() + " " + transfer.getDescription());
		// Transfer the commission first
		Account myInternalAccount = new Account(myUserId, Account.TYPE_INTERNAL);
		Account myExternalAccount = new Account(myUserId, Account.TYPE_EXTERNAL);
		Account comAccount = new Account(userService.getCommissionUserId(), Account.TYPE_INTERNAL);
		BigDecimal comAmount = calculateCommission(amount);
		transfer.setAccountCredit(accountService.operateTransfer(comAccount, comAmount, true));
		transfer.setAccountDebit(accountService.operateTransfer(myInternalAccount, calculateCommission(amount), false));
		Transfer comTransfer = transferRepository.save(transfer);
		if (comTransfer == null) return null;
		// Operate the transfer then
		transfer.setAccountCredit(accountService.operateTransfer(myExternalAccount, amount, true));
		transfer.setAccountDebit(accountService.operateTransfer(myInternalAccount, amount, false));
		return transferRepository.save(transfer);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ArrayList<Transfer> getMyTransferList() throws NoAuthenticatedUserException
	{
		logger.info("getMyTransferList");
		int myUserId = userService.getAuthenticatedUser().getId();
		List<Transfer> fullList = transferRepository.findAll();
		ArrayList<Transfer> myList = new ArrayList<Transfer>();
		for (Transfer t : fullList) if (t.getAccountDebit().getUserId().getId() == myUserId) myList.add(t);
		return myList;
	}
}
