package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
