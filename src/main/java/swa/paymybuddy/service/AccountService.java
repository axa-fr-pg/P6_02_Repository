package swa.paymybuddy.service;

import swa.paymybuddy.model.Account;

public interface AccountService {

	Account addInternal(int userId);

	Account addExternal(int userId);
}
