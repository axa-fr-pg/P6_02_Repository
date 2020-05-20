package swa.paymybuddy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RelationId implements Serializable {

	private static final long serialVersionUID = 8280251507410843733L;
	int userCredit;
	int userDebit;
}
