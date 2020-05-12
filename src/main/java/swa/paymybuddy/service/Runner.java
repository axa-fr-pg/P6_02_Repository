package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@Service
public class Runner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	@Autowired
    private UserRepository userRepository;
	
    @Override
    public void run(String[] args) throws Exception {
    	    	
        userRepository.deleteAll();

        userRepository.save(new User("UserA", "$2y$10$Tizt8PWuzXwth.UGEU2PHewSaJP4PjCXxygL3SgCpdmgVHQy/DZX6", 0)); //PassA
        userRepository.save(new User("UserB", "$2y$10$w6WCcjlYZLJI9MmDNZN.HuAS9/vIm/SoRghEI5ia6UKKfO7.4r04C", 0)); //PassB

        userRepository.findAll().forEach((user) -> {
            logger.info("{}", user.getEmail());
        });
    }
}
