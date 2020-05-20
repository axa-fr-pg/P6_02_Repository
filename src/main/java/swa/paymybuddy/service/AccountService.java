package swa.paymybuddy.service;

import java.math.BigDecimal;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;

public interface AccountService {

	BigDecimal calculateBalanceAfterTransfer(BigDecimal initialBalance, BigDecimal amountToOperate, boolean credit) 
			throws TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException;
	
	Account operateTransfer(Account account, BigDecimal amount, boolean credit) 
			throws TransferAmountGreaterThanAccountBalanceException, InvalidTransferAmountException;
}
