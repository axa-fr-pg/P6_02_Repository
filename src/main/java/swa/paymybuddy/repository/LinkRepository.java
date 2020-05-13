package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.Link;
import swa.paymybuddy.model.LinkId;

public interface LinkRepository extends CrudRepository<Link, LinkId> {

}
