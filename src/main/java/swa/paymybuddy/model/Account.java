package swa.paymybuddy.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NaturalId;

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
@IdClass(AccountId.class)
public class Account {

	@Id
	@ManyToOne
    User user;
	
	@Id
	@Column(columnDefinition = "TINYINT", nullable = true)
	int type; // 0 : application internal, 1 : external bank account, other values : undefined
	
	@Column(length = 30, nullable = false)
	String name;
	
	@Column(columnDefinition = "DECIMAL(9,3)", nullable = false)
	BigDecimal balance;
	
	@NaturalId
	@Column(length = 11, nullable = false)
	String bic; // only for external accounts

	
	@NaturalId 
	@Column(length = 34, nullable = false)
	String iban; // only for external accounts
}
