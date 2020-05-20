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
public class AccountId implements Serializable {

	private static final long serialVersionUID = -6431851748755572351L;
	int user;
	int type;
}
