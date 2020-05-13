package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.Link;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.LinkRepository;
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.repository.UserRepository;

@Service
public class Runner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	@Autowired
    private UserRepository userRepository;
	@Autowired
    private LinkRepository linkRepository;
	@Autowired
    private TransferRepository transferRepository;
	
    @Override
    public void run(String[] args) throws Exception {
    	    	
        transferRepository.deleteAll();		
        linkRepository.deleteAll();
        userRepository.deleteAll();

        /*
        User u1 = userRepository.save(new User(1, 0, "UserA", 
        		"$2y$10$Tizt8PWuzXwth.UGEU2PHewSaJP4PjCXxygL3SgCpdmgVHQy/DZX6", false)); //PassA
        User u2 = userRepository.save(new User(2, 0, "UserB", 
        		"$2y$10$w6WCcjlYZLJI9MmDNZN.HuAS9/vIm/SoRghEI5ia6UKKfO7.4r04C", true)); //PassB
        
        Link l1to2 = linkRepository.save(new Link(u1, u2));

        transferRepository.save(new Transfer(l1to2, 1, "my personal transfer comment")); 
        */
    }
}
