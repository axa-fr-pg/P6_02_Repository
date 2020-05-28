package swa.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RunnerServiceImpl implements RunnerService {

    private static final Logger logger = LoggerFactory.getLogger(RunnerServiceImpl.class);

    /*
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private RelationRepository relationRepository;
	
	@Autowired
    private TransferRepository transferRepository;

	@Autowired
    private PersistentLoginsRepository persistentLoginsRepository;
    */

	private boolean useTestDatabase() {
    	StackTraceElement[] trace = Thread.currentThread().getStackTrace();
    	String caller = trace[trace.length-1].getClassName();
    	if (caller == "swa.paymybuddy.PayMyBuddyApplication") return false;
    	return true;
	}

    @Override
    public void run(String... args) throws Exception {
    	    	
		logger.info("run ");

		if (useTestDatabase()) return;
		
		/*
		 * Initialize production database with some values
		 * (replaced with SQL script initialization after successful JPA database generation)
		 *
		transferRepository.deleteAllInBatch();
		accountRepository.deleteAllInBatch();
		relationRepository.deleteAllInBatch();
		persistentLoginsRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
		
        User u0 = userRepository.save(new User(0, 0, User.EMAIL_COM_USER, ""));
        User u1 = userRepository.save(new User(0, 0, "philippe.gey@axa.fr", 
        		"$2y$10$MNyoW.RghhyEhWEV4Ic61OX6unMMjs2L.PsUQ8MGSmRfUPCLAaZ4W")); // Password is axa
        User u2 = userRepository.save(new User(0, 0, "my.wife@home.fr", 
        		"$2y$10$3oN0znyPYd3ulsyL5r6PCuKsDCjuUl1Y.cnZExjesp2PAnVS5uPrC")); // Password is home
        
        Account aInt0 = accountRepository.save(new Account(u0, Account.TYPE_INTERNAL, new BigDecimal(1000), null, null));
        Account aInt1 = accountRepository.save(new Account(u1, Account.TYPE_INTERNAL, new BigDecimal(100000), null, null));
        Account aInt2 = accountRepository.save(new Account(u2, Account.TYPE_INTERNAL, new BigDecimal( 50000), null, null));
        Account aExt1 = accountRepository.save(new Account(u1, Account.TYPE_EXTERNAL, new BigDecimal(900000), "my_bic", "this_is_my_iban"));
        
        relationRepository.save(new Relation(u1, u2));
        relationRepository.save(new Relation(u2, u1));
        
        transferRepository.save(new Transfer(aInt1, aExt1, null, 0, "Loading my money", new BigDecimal(150000)));
        transferRepository.save(new Transfer(aInt0, aInt1, null, 0, "Loading my money", new BigDecimal(750)));        
        transferRepository.save(new Transfer(aInt2, aInt1, null, 0, "Sharing with my wife", new BigDecimal(50000)));
        transferRepository.save(new Transfer(aInt0, aInt1, null, 0, "Sharing with my wife", new BigDecimal(250)));
        */
    }
}
