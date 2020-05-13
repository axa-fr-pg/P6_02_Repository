package swa.paymybuddy.model;

import java.io.Serializable;

public class TransferId implements Serializable {

	AccountId accountCredit;
	AccountId accountDebit;
	int transferId;
}
