package swa.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersistentLoginsTest {

	  @Autowired 
	  private PersistentLoginsRepository persistentLoginsRepository;
	  
	  @Test
	  void injectedRepositoryIsNotNull(){
	    assertThat(persistentLoginsRepository).isNotNull();
	  }	  
}
