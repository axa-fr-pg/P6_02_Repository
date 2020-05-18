package swa.paymybuddy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class LinkId implements Serializable {

	int userCredit;
	int userDebit;
}
