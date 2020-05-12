package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
	
	public static final String ROLE_APP_USER = "ROLE_APP_USER";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE) 
	private Integer id;
	
	@Column(length = 30)
	private String email;

	@Column(length = 60)
	private String password;

	@Column(columnDefinition = "TINYINT")
	private Integer social_network;
	
	private User() {
		// Required for JPA thus private
	}

	public User(String email, String password, Integer socialNetwork) {
		this.email = email;
		this.password = password;
		social_network = socialNetwork;
	}

}
