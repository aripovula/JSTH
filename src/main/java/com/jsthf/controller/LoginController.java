package com.jsthf.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsthf.model.UserAuth;
import com.jsthf.other.FlashMessage;
import com.jsthf.service.UserAuthService;
import com.jsthf.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserAuthService userAuthService;
	
	@Autowired
	private UserService userService;

	
	private static Logger myLogger = Logger
			.getLogger(FCExItemController.class.getName());

	
	@GetMapping(value={"/", "/login"})
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/registration")
	public String registration(Model model) {

		if (!model.containsAttribute("user")) {
			model.addAttribute("user", new UserAuth());
		}
		return "registration";
	}


	
	@PostMapping("/registration")
	public String createNewUser(@Valid UserAuth user, BindingResult result, RedirectAttributes redirectAttributes) {
		
		UserAuth userExists = userAuthService.findUserByEmail(user.getEmail());
		if (userExists != null) {

			// Add user if invalid was received
			redirectAttributes.addFlashAttribute("user", user);

			redirectAttributes.addFlashAttribute("flash", new FlashMessage(
					"There is already a user registered with the username provided !", FlashMessage.Status.FAILURE));

			return "redirect:/registration";
		}
		
		if (result.hasErrors()) {

			// Include validation errors upon redirect
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);

			// Add user if invalid was received
			redirectAttributes.addFlashAttribute("user", user);

			// Redirect back to the form
			return "redirect:/registration";
		}


		userAuthService.saveUser(user);
		
		redirectAttributes.addFlashAttribute("flash", new FlashMessage("User has been successfully registered !", FlashMessage.Status.SUCCESS));

		return "redirect:/login";
	}

}