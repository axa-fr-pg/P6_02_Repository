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
    private UserRepository repository;
	
    @Override
    public void run(String[] args) throws Exception {
    	
        repository.deleteAll();

        repository.save(new User("User A"));
        repository.save(new User("User B"));

        repository.findAll().forEach((user) -> {
            logger.info("{}", user.getEmail());
        });
    }
}
