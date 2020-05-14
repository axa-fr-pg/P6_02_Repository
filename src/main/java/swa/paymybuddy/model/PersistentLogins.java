package swa.paymybuddy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class PersistentLogins {

	@Id
	@Column(length = 64)
	String series;

	@Column(length = 30) // LIEN POSSIBLE ?
	String username;

	@Column(length = 64)
	String token;

	@Temporal(TemporalType.TIMESTAMP)
    Date lastUsed;
}
