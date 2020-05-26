package swa.paymybuddy.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table( indexes = { // Indexes specified explicitly because JPA misses some indexes when joining columns with foreign keys
		@Index(name = "IFK_user_credit_id",  columnList="user_credit_id", unique = false),
		@Index(name = "IFK_user_debit_id",  columnList="user_debit_id", unique = false)
	})
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RelationId.class)
public class Relation {

	@Id
	@ManyToOne
    @JoinColumn(name = "user_credit_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_user_credit_id"))
	User userCredit;
	
	@Id
	@ManyToOne
    @JoinColumn(name = "user_debit_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_user_debit_id"))
    User userDebit;
	
	public Relation(int creditUserId, int debitUserId) 
	{
		userCredit = new User(creditUserId);
		userDebit = new User(debitUserId);
	}
}
