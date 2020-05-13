package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.TransferId;

public interface TransferRepository extends CrudRepository<Transfer, TransferId> {

}
