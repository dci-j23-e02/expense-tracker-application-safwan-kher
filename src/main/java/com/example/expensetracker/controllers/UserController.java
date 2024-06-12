package com.example.expensetracker.controllers;


import com.example.expensetracker.models.User;
import com.example.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

  @Autowired
  private UserService userService;


  @GetMapping("/")
  public String home(){
    return "home";
  }


  @GetMapping("/login")
  public String login(Model model){
    model.addAttribute("errorMessage", "Invalid Credentials !!" );
    return "login"; // Return login view
  }


  @GetMapping("/logout")
  public String logout(){
    return "redirect:/login?logout"; // Redirect to login page with logout parameter
  }

  @GetMapping("/signup")
  public String showSignupForm(Model model){
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping("/signup")
  public String signup(User user){
    userService.saveUser(user);
    return "redirect:/login";

  }
}
