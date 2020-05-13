package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;

public interface AccountRepository extends CrudRepository<Account, AccountId> {

}
