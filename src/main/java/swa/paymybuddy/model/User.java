package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Entity
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name="user_sequence", initialValue=1)
public class User {
	
	public static final String ROLE_APP_USER = "ROLE_APP_USER";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE) 
	Integer id;
	
	@Column(columnDefinition = "TINYINT", nullable = true)
	Integer type; 
	
	@Column(length = 30, nullable = false)
	String email;

	@Column(length = 60, nullable = false)
	String password;

	@Column(columnDefinition = "TINYINT", nullable = true)
	boolean permanent;

	public User( int type, String email, String password ) {
		this.type = type;
		this.email = email;
		this.password = password;
	}
}
