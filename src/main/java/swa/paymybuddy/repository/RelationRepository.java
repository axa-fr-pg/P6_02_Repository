package swa.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.RelationId;

public interface RelationRepository extends CrudRepository<Relation, RelationId> {

}
