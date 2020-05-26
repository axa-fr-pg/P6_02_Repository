package swa.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import swa.paymybuddy.model.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

}
