package swa.paymybuddy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(indexes = { @Index(name = "FK_username", columnList = "username") })
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class PersistentLogins {

	@Id
	@Column(length = 64)
	String series;

	@ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "email", foreignKey = @ForeignKey(name = "FK_email"))
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
	
	public PersistentLogins(String series, User username, String token, Date lastUsed) 
	{
		this.series = series;
		this.username = new User(username.getId(), username.getType(), username.getEmail(), username.getPassword());
		this.token = token;
		this.lastUsed = (Date) lastUsed.clone();
	}
}
