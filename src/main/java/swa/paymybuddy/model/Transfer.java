package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

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
    User userCredit;
	
	@Id
	@ManyToOne
    User userDebit;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE) 
	Integer transferId;

	@Column(length = 100, nullable = false)
	String email;
}
