package swa.paymybuddy.service;

import java.math.BigDecimal;
import swa.paymybuddy.model.Transfer;

public interface TransferService {

	Transfer transferInternal(Transfer transfer) 
			throws 	TransferOutsideOfMyNetworkException, TransferAmountGreaterThanAccountBalanceException, 
					InvalidTransferAmountException, NoAuthenticatedUserException;

}
