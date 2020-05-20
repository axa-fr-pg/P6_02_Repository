package swa.paymybuddy.service;

import java.math.BigDecimal;
import swa.paymybuddy.model.Transfer;

public interface TransferService {

	Transfer transferInternal(int myFriendId, String description, BigDecimal amount) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException;

}
