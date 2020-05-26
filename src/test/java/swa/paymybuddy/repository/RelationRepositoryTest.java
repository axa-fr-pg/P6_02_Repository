package swa.paymybuddy.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RelationRepositoryTest {

	  @Autowired private RelationRepository relationRepository;
	  
	  @Test
	  void injectedRepositoryIsNotNull(){
	    assertThat(relationRepository).isNotNull();
	  }	  
}
