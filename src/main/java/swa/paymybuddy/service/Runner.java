package swa.paymybuddy.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.repository.UserRepository;

@Service
public class Runner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private RelationRepository relationRepository;
	
	@Autowired
    private TransferRepository transferRepository;
	
    @Override
    public void run(String... args) throws Exception {
    	    	
		logger.info("run ");
		/*
        User u1 = userRepository.save(new User(1, 0, "UserA", 
        		"$2y$10$Tizt8PWuzXwth.UGEU2PHewSaJP4PjCXxygL3SgCpdmgVHQy/DZX6")); //PassA
        User u2 = userRepository.save(new User(2, 0, "UserB", 
        		"$2y$10$w6WCcjlYZLJI9MmDNZN.HuAS9/vIm/SoRghEI5ia6UKKfO7.4r04C")); //PassB
        
        if (u1 == null || u2 == null) return;
        
        Account a1 = accountRepository.save(new Account(u1.getId(), Account.TYPE_INTERNAL));
        Account a2 = accountRepository.save(new Account(u2.getId(), Account.TYPE_INTERNAL));
        
        Relation r12 = relationRepository.save(new Relation(u1, u2));
        
        transferRepository.save(new Transfer(u1.getId(), u2.getId(), Account.TYPE_INTERNAL, "my personal transfer comment A", new BigDecimal(0)));    
        transferRepository.save(new Transfer(u1.getId(), u2.getId(), Account.TYPE_INTERNAL, "my personal transfer comment B", new BigDecimal(0)));    
        transferRepository.save(new Transfer(u1.getId(), u2.getId(), Account.TYPE_INTERNAL, "my personal transfer comment C", new BigDecimal(0)));    
        transferRepository.save(new Transfer(u1.getId(), u2.getId(), Account.TYPE_INTERNAL, "my personal transfer comment D", new BigDecimal(0)));    
*/
    }
}
