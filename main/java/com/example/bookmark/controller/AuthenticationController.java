package com.example.bookmark.controller;

import com.example.bookmark.dto.RegistrationDTO;
import com.example.bookmark.model.User;
import com.example.bookmark.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        if (!model.containsAttribute("registrationDTO")) {
            model.addAttribute("registrationDTO", new RegistrationDTO());
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("registrationDTO") RegistrationDTO registrationDTO,
                               BindingResult result, 
                               RedirectAttributes redirectAttributes,
                               Model model) { 

        if (result.hasErrors()) { 
            model.addAttribute("registrationDTO", registrationDTO); 
            model.addAllAttributes(result.getModel()); 
            return "signup";
        }

        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            model.addAttribute("errorMessage", "Username already exists!"); 
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists!");
            redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
            return "redirect:/signup";
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,
    		@RequestParam(value = "error", required = false) String error,
    		@RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out.");
        }
        return "login";
    }
}