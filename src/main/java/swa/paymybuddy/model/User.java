package swa.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

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
@Table(indexes = { @Index(name = "email_index", columnList = "email", unique=true) })
public class User implements Serializable {
	
	private static final long serialVersionUID = 955065855418183406L;

	public static final String ROLE_APP_USER = "ROLE_APP_USER";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@Column(columnDefinition = "TINYINT", nullable = true)
	int type; // 0 : application, other : social network code
	
	@NaturalId
	@Column(length = 30, nullable = false)
	String email;

	@Column(length = 60, nullable = false)
	String password;
	
	public User(int knownId)
	{
		id = knownId;
	}
}
