package com.example.expensetracker.controllers;


import com.example.expensetracker.models.User;
import com.example.expensetracker.service.UserService;
import jakarta.mail.search.SearchTerm;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

  @Autowired
  private UserService userService;


  @GetMapping("/")
  public String home(Principal principal){
    User user = userService.findByUsername(principal.getName());
    if(user.getRoles().contains("ROLE_ADMIN")){
      return "redirect:/admin-home";
    }

    return "home";
  }
  @GetMapping("/admin-home")
  public String adminHome(){
    return "admin-home";// return the view directly
  }

  @GetMapping("/login")
  public String login(Model model){
    model.addAttribute("errorMessage", "Invalid Credentials !!" );
    return "login"; // Return login view
  }


  @GetMapping("/logout")
  public String logout() {

    return "redirect:/login?logout"; // Redirect to login page with logout parameter
  }

  @GetMapping("/signup")
  public String showSignupForm(Model model){
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping("/signup")
  public String signup(User user, Model model){
    boolean isUserSaved =  userService.saveUser(user);
    if(!isUserSaved){
      model.addAttribute("errorMessage",
          "Failed to send verification email. Please add a correct email !");
      return "redirect:/signup";
    }
    return "redirect:/login";

  }

  @GetMapping("/verify")
  public String verifyUser(
      @RequestParam("token") String token
  ){
    userService.verifyUser(token);
    return "redirect:/login?verified";
  }

  @GetMapping("/assign-admin")
  public String showAdminRoleForm(){
    return "assign-admin";
  }


  @PostMapping("/assign-admin")
  public String assignAdminRole(String username, Model model){
    User user = userService.findByUsername(username);
    if(user != null){
      Set<String> roles = new HashSet<>(user.getRoles());
      roles.add("ROLE_ADMIN");
      user.setRoles(roles);
      userService.updateUserRoles(user); // new added

      // log role assignment
      System.out.println("Roles assigned to user: "+ user.getRoles());

      model.addAttribute("successMessage", "Admin role assigned successfully.");
    }else{
      model.addAttribute("errorMessage", "User not found.");
    }
    return "redirect:/admin-home";
  }
}
