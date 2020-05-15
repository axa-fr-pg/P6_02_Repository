package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@SequenceGenerator(name="transferSequence", sequenceName="transfer_sequence")
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
	Link link; // Foreign key to the link consists in both credit & debit user ids (within primary key)
	
	@Id
	@GeneratedValue(generator="transferSequence") // I didn't find out how to define sequence as INT instead of BIGINT
	int transferId; 

	@Column(length = 100, nullable = false)
	String description;
}
