package swa.paymybuddy.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransferId.class)
public class Transfer {

	@Id
	@ManyToOne
	Account accountCredit;
	
	@Id
	@ManyToOne
	Account accountDebit;

	@ManyToOne
    @JoinColumns({
        @JoinColumn(name="account_credit_user_id", referencedColumnName="user_credit_id", insertable = false, updatable = false),
        @JoinColumn(name="account_debit_user_id", referencedColumnName="user_debit_id", insertable = false, updatable = false)
    })
	Relation relation; // Foreign key to the relation consists in both credit & debit user ids (within primary key)
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int transferId; 

	@Column(length = 100, nullable = false)
	String description;
	
	@Column(columnDefinition = "DECIMAL(9,3)", nullable = false)
	BigDecimal amount;
	
	public Transfer(int userCreditId, int userDebitId, int accountType, String description, BigDecimal amount)
	{
		User userCredit = new User(userCreditId);
		User userDebit = new User(userDebitId);
		this.accountCredit = new Account(userCredit.getId(), accountType);
		this.accountDebit = new Account(userDebit.getId(), accountType);;
		this.relation = new Relation(userCredit.getId(), userDebit.getId());
		this.description = description;
		this.amount = amount;
	}
}
