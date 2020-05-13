package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
    User userCredit;
	
	@Id
	@ManyToOne
    User userDebit;
	
	@Id
	@GeneratedValue(generator="transferSequence") 
	int transferId;

	@Column(length = 100, nullable = false)
	String email;
}
