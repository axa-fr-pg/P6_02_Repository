package swa.paymybuddy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TransferId implements Serializable {

	AccountId accountCredit;
	AccountId accountDebit;
	int transferId;
}
