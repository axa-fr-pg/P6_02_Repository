package swa.paymybuddy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NaturalId;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = { @Index(name = "username_index", columnList = "username") })
public class PersistentLogins {

	@Id
	@Column(length = 64)
	String series;

	@ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "email")
	@NaturalId
	User username;

	@Column(length = 64)
	String token;

	@Temporal(TemporalType.TIMESTAMP)
    Date lastUsed;
	
	public Date getLastUsed()
	{
		return (Date) lastUsed.clone();
	}
	
	public void setLastUsed(Date l)
	{
		lastUsed = (Date) l.clone();
	}
}
