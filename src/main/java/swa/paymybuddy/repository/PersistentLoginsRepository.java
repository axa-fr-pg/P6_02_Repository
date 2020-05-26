package swa.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.PersistentLogins;

@Transactional
public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, String> {

}
