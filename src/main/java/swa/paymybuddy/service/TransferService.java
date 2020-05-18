package swa.paymybuddy.service;

import java.math.BigDecimal;
import swa.paymybuddy.model.Transfer;

public interface TransferService {

	Transfer transferInternal(int userCreditId, int userDebitId, String description, BigDecimal amount);

}
