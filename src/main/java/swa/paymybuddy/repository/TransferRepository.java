package swa.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.TransferId;

@Transactional
public interface TransferRepository extends JpaRepository<Transfer, TransferId> {

}
