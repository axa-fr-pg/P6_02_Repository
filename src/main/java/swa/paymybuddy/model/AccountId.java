package swa.paymybuddy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountId implements Serializable {

	int user;
	int type;
}
