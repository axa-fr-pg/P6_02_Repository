package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class Runner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

/*	@Autowired
    private UserRepository userRepository;
	@Autowired
    private RelationRepository relationRepository;
	@Autowired
    private TransferRepository transferRepository;*/
	
    @Override
    public void run(String... args) throws Exception {
    	    	
		logger.info("run ");
		
		/*
        transferRepository.deleteAll();		
        relationRepository.deleteAll();
        userRepository.deleteAll();
        
        User u1 = userRepository.save(new User(1, 0, "UserA", 
        		"$2y$10$Tizt8PWuzXwth.UGEU2PHewSaJP4PjCXxygL3SgCpdmgVHQy/DZX6", false)); //PassA
        User u2 = userRepository.save(new User(2, 0, "UserB", 
        		"$2y$10$w6WCcjlYZLJI9MmDNZN.HuAS9/vIm/SoRghEI5ia6UKKfO7.4r04C", true)); //PassB
        
        relation l1to2 = relationRepository.save(new relation(u1, u2));

        transferRepository.save(new Transfer(l1to2, 1, "my personal transfer comment")); 
        */
    }
}
