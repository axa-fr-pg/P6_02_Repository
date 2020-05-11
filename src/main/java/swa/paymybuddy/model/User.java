package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
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
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSocial_network() {
		return social_network;
	}

	public void setSocial_network(Integer social_network) {
		this.social_network = social_network;
	}
	
}
