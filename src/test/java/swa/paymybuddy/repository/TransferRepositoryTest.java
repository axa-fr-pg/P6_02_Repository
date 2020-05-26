package swa.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransferRepositoryTest {

	  @Autowired private TransferRepository transferRepository;
	  
	  @Test
	  void injectedRepositoryIsNotNull(){
	    assertThat(transferRepository).isNotNull();
	  }	  
}
