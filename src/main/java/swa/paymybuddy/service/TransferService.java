package swa.paymybuddy.service;

import swa.paymybuddy.model.Transfer;

public interface TransferService {

	Transfer transferInternal(int userCreditId, int userDebitId, String description);

}
