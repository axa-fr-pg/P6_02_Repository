package swa.paymybuddy.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GenericGenerator;

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
@SequenceGenerator(name="transferSequence", sequenceName="transfer_sequence")
public class Transfer {

	@Id
	@ManyToOne
    @JoinColumns(
    	foreignKey = @ForeignKey(name = "FK_account_credit"),
    	value = {
	        @JoinColumn(name="account_credit_user_id", referencedColumnName="user_id"),
	        @JoinColumn(name="account_credit_type", referencedColumnName="type", columnDefinition = "TINYINT")
	    }
    )
	Account accountCredit;
	
	@Id
	@ManyToOne
    @JoinColumns(
        	foreignKey = @ForeignKey(name = "FK_account_debit"),
        	value = {
    	        @JoinColumn(name="account_debit_user_id", referencedColumnName="user_id"),
    	        @JoinColumn(name="account_debit_type", referencedColumnName="type", columnDefinition = "TINYINT")
    	    }
        )
	Account accountDebit;

	@ManyToOne
    @JoinColumns(
    	foreignKey = @ForeignKey(name = "FK_relation"),
    	value = {
	        @JoinColumn(name="account_credit_user_id", referencedColumnName="user_credit_id", insertable = false, updatable = false),
	        @JoinColumn(name="account_debit_user_id", referencedColumnName="user_debit_id", insertable = false, updatable = false)
    	}
    )
	Relation relation;
	
	@Id
	@GeneratedValue(generator="transferSequence") // IDENTITY can't be used here due to composite primary key
	int transferId;

	@Column(length = 100, nullable = false)
	String description;
	
	@Column(columnDefinition = "DECIMAL(9,3)", nullable = false)
	BigDecimal amount;
	
	public Transfer(int userCreditId, int userDebitId, int accountType, String description, BigDecimal amount)
	{
		this.accountCredit = new Account(userCreditId, accountType);
		this.accountDebit = new Account(userDebitId, accountType);;
		this.relation = new Relation(userCreditId, userDebitId);
		this.description = description;
		this.amount = amount;
	}
}
