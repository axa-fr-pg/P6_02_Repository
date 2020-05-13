package swa.paymybuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import swa.paymybuddy.model.User;
import swa.paymybuddy.repository.UserRepository;

@RestController
@RequestMapping("")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
    
    @GetMapping("/register")
    public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/register.html", model);
    }
    
	@PostMapping("/register")
	public ResponseEntity<String> registerNewUser(@RequestParam String email, @RequestParam String password) {
		logger.info("POST new user" + email);
		User u = new User(1, 0, email, password, false);
		if (userRepository.save(u) == null) return null;
		return new ResponseEntity<String>(email + " has been registered successfully", HttpStatus.CREATED);
	}
	
}
