package swa.paymybuddy.service;

import swa.paymybuddy.model.Transfer;

public interface TransferService {

	Transfer transferInternal(Transfer transfer) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException;

	Transfer transferFromOutside(Transfer transfer)
			throws InvalidTransferAmountException, NoAuthenticatedUserException, TransferAmountGreaterThanAccountBalanceException;

	Transfer transferToOutside(Transfer transfer)
			throws InvalidTransferAmountException, NoAuthenticatedUserException, TransferAmountGreaterThanAccountBalanceException;
	
}
