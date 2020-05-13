package swa.paymybuddy.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@IdClass(AccountId.class)
public class Account {

	@Id
	@ManyToOne
    User user;
	
	// Account type :
	// 0 = native application account
	// 1 = external bank account identified with BIC and IBAN
	@Id
	@Column(columnDefinition = "TINYINT", nullable = true)
	int type; 
	
	@Column(length = 30, nullable = false)
	String name;
	
	@Column(columnDefinition = "DECIMAL(9,3)", nullable = false)
	BigDecimal balance;
	
	// Required only for external accounts
	@Column(length = 11, nullable = false)
	String bic;

	// Required only for external accounts
	@Column(length = 34, nullable = false)
	String iban;
}
