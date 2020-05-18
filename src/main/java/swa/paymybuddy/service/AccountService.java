package swa.paymybuddy.service;

import java.math.BigDecimal;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;

public interface AccountService {

	Account addInternal(int userId);

	Account addExternal(int userId);
	
	Account operateTransfer(AccountId accountId, BigDecimal amount, boolean credit) 
			throws TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException;
	
}
