package swa.paymybuddy.service;

import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.service.exception.InvalidTransferAmountException;
import swa.paymybuddy.service.exception.NoAuthenticatedUserException;
import swa.paymybuddy.service.exception.TransferAmountGreaterThanAccountBalanceException;
import swa.paymybuddy.service.exception.TransferOutsideOfMyNetworkException;

public interface TransferService {

	Transfer transferInternal(Transfer transfer) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException;

	Transfer transferFromOutside(Transfer transfer)
			throws InvalidTransferAmountException, NoAuthenticatedUserException, TransferAmountGreaterThanAccountBalanceException;

	Transfer transferToOutside(Transfer transfer)
			throws InvalidTransferAmountException, NoAuthenticatedUserException, TransferAmountGreaterThanAccountBalanceException;
	
}
