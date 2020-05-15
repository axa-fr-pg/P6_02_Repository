package swa.paymybuddy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransferId implements Serializable {

	AccountId accountCredit;
	AccountId accountDebit;
	int transferId;
}
