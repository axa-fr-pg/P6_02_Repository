package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@SequenceGenerator(name="userSequence", sequenceName="user_sequence")
public class User {
	
	public static final String ROLE_APP_USER = "ROLE_APP_USER";

	@Id
	@GeneratedValue(generator="userSequence") 
	int id;
	
	// User type :
	// 0 = native application user
	// other = specific code per social network
	@Column(columnDefinition = "TINYINT", nullable = true)
	int type; 
	
	@Column(length = 30, nullable = false, unique = true)
	String email;

	@Column(length = 60, nullable = false)
	String password;
}
