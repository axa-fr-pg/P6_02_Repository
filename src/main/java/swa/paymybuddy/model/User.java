package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE) 
	private Integer id;
	
	@Column(length = 30)
	private String email;
	
	@Column(columnDefinition = "TINYINT")
	private Integer social_network;
	
	private User() {
	}

	public User(String email) {
		this.email = email;
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

}
