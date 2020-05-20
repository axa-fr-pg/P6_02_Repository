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
public class TransferId implements Serializable {

	private static final long serialVersionUID = -1131261621032753862L;
	AccountId accountCredit;
	AccountId accountDebit;
	int transferId;
}
