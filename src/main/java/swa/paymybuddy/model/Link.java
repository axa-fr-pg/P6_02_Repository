package swa.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(LinkId.class)
public class Link {

	@Id
	@ManyToOne
    private User userId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE) 
	int linkId;

}
