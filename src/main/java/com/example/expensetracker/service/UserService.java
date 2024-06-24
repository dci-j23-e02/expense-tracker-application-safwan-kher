package com.example.expensetracker.service;

import com.example.expensetracker.models.Role;
import com.example.expensetracker.models.User;
import com.example.expensetracker.models.VerificationToken;
import com.example.expensetracker.repositories.RoleRepository;
import com.example.expensetracker.repositories.UserRepository;
import com.example.expensetracker.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private VerificationTokenRepository tokenRepository;

  @Autowired
  private JavaMailSender mailSender;

  @Transactional
  public boolean saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName("ROLE_USER");
    if (userRole == null) {
      userRole = new Role("ROLE_USER");
      roleRepository.save(userRole);
    }
    roles.add(userRole);
    user.setRoles(roles);
    userRole.addUser(user); // Ensure bidirectional relationship
    try {
      userRepository.save(user);
      sendVerificationEmail(user);
    } catch (MailException e) {
      // MailException will be thrown if the email is not sent
      // Log the exception (optional)
      System.out.println("Failed to send verification email:" + e.getMessage());
      return false;
    }
    return true;
  }

  @Transactional
  public void updateUserRoles(User user) {
    userRepository.save(user);
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    // Log user details
    System.out.println("User found: " + user.getUsername() + ", Verified: " + user.isVerified() + ", Roles: " + user.getRoles());

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())) // Convert roles to authorities
        .accountLocked(!user.isVerified())
        .build();
  }

  public void createOrUpdateVerificationToken(User user, String token) {
    VerificationToken verificationToken = tokenRepository.findByUsername(user.getUsername());
    if (verificationToken == null) {
      verificationToken = new VerificationToken();
      verificationToken.setUser(user);
    }
    verificationToken.setToken(token);
    verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
    tokenRepository.save(verificationToken);
  }

  public VerificationToken getVerificationToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public void verifyUser(String token) {
    VerificationToken verificationToken = getVerificationToken(token);
    if (verificationToken != null // does exist
        && verificationToken.getExpiryDate().isAfter(LocalDateTime.now()) // not expired
    ) {
      User user = verificationToken.getUser();
      user.setVerified(true);
      userRepository.save(user);
      // tokenRepository.delete(verificationToken); // we might keep it for our future logs

      // Log verification success
      System.out.println("User verified: " + user.getUsername());
    } else {
      // Log verification failure
      System.out.println("Verification token is invalid or expired");
    }
  }

  private void sendVerificationEmail(User user) throws MailException {
    String token = UUID.randomUUID().toString();
    createOrUpdateVerificationToken(user, token);
    String recipientAddress = user.getEmail();
    String subject = "Email Verification";
    String confirmationUrl = "http://localhost:8085/verify?token=" + token;
    String message = "Please click the link below to verify your email address:\n" + confirmationUrl;
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }
}