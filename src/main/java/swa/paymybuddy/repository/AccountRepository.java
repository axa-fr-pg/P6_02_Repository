package swa.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.AccountId;

@Transactional
public interface AccountRepository extends JpaRepository<Account, AccountId> {

}
