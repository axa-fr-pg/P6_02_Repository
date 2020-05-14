package swa.paymybuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swa.paymybuddy.service.UserService;

@RestController
@RequestMapping("")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<String> registerNewUser(@RequestParam String email, @RequestParam String password) {
		logger.info("Registering new user " + email);
		userService.registerUserInternal(email, password);
		return new ResponseEntity<String>("User " + email + " created successfully", HttpStatus.CREATED);
	}	
}
