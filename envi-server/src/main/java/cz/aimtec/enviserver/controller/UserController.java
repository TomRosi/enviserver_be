package cz.aimtec.enviserver.controller;

import cz.aimtec.enviserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/login")
	public @ResponseBody User getByUsername(@RequestBody LoginForm loginForm) {
		if (loginForm.getUsername() != null && loginForm.getPassword() != null && !loginForm.getUsername().isEmpty() && !loginForm.getPassword().isEmpty()) {
			if(userRepository.findByUsername(loginForm.getUsername()).isPresent()) {
				if(userRepository.findByUsername(loginForm.getUsername()).get().getPassword().equals(loginForm.getPassword())) {
					System.out.println(userRepository.getOneByUsername(loginForm.getUsername()));
					return userRepository.getOneByUsername(loginForm.getUsername());
				} else {
					return null; //throw new BadCredentialsException();
				}
			} else {
				return null; //throw new BadCredentialsException();
			}
		} else {
			return null; //throw new BadCredentialsException();
		}
	}


}