package swa.paymybuddy.service;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swa.paymybuddy.model.Account;
import swa.paymybuddy.model.Relation;
import swa.paymybuddy.model.Transfer;
import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.AccountRepository;
import swa.paymybuddy.repository.PersistentLoginsRepository;
import swa.paymybuddy.repository.RelationRepository;
import swa.paymybuddy.repository.TransferRepository;
import swa.paymybuddy.repository.UserRepository;

@Service
public class RunnerServiceImpl implements RunnerService {

    private static final Logger logger = LoggerFactory.getLogger(RunnerServiceImpl.class);

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
		 */
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
        
        Account aInt0 = accountRepository.save(new Account(u0, Account.TYPE_INTERNAL, new BigDecimal(250), "", ""));
        Account aInt1 = accountRepository.save(new Account(u1, Account.TYPE_INTERNAL, new BigDecimal(100000), "", ""));
        Account aInt2 = accountRepository.save(new Account(u2, Account.TYPE_INTERNAL, new BigDecimal( 50000), "", ""));
        Account aExt1 = accountRepository.save(new Account(u1, Account.TYPE_EXTERNAL, new BigDecimal(900000), "my_bic", "this_is_my_iban"));
        
        relationRepository.save(new Relation(u1, u2));
        relationRepository.save(new Relation(u2, u1));
        
        Transfer t1 = transferRepository.save(new Transfer(aInt1, aExt1, null, 0, "Loading my money", new BigDecimal(150000)));
//        transferRepository.save(new Transfer(aInt0, aInt1, null, 0, "Commission for transfer", new BigDecimal(750)));        
        Transfer t2 = transferRepository.save(new Transfer(aInt2, aInt1, null, 0, "Sharing with my wife", new BigDecimal(50000)));
    }
}
