package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.LinkId;
import swa.paymybuddy.model.User;

public interface LinkRepository extends CrudRepository<User, LinkId> {

}
