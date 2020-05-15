package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.PersistentLogins;

public interface PersistentLoginsRepository extends CrudRepository<PersistentLogins, String> {

}
