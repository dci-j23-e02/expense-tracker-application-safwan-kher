package com.example.expensetracker.service;

import com.example.expensetracker.models.VerificationToken;
import com.example.expensetracker.repositories.VerificationTokenRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.expensetracker.models.User;
import com.example.expensetracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private VerificationTokenRepository tokenRepository;

  @Autowired
  private JavaMailSender mailSender;


  @Transactional
  public boolean saveUser(User user){
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    user.setRoles(roles);
   try{
     userRepository.save(user);
     sendVerificationEmail(user);
   }catch(MailException e){ // MailException will be thrown if the email is not sent
     //Log the exception (optional)
     System.out.println("Failed to send verification email:"+e.getMessage());
     return false;
   }
    return true;
  }


  public User findByUsername(String username){
    return userRepository.findByUsername(username);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // find our user in the database :
    User user = userRepository.findByUsername(username);

    if(user == null){
      throw new UsernameNotFoundException("User not found");
    }
//    .accountLocked(!user.isVerified()) : is alternative of the below
//    if(!user.isVerified()){
//      throw new UsernameNotFoundException("User email is not verified");
//    }

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(user.getRoles().toArray(new String[0])) // We  can add set roles/authorities here
        .accountLocked(!user.isVerified())
        .build();
  }


  public void createVerificationToken(User user, String token){
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setUser(user);
    verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
    tokenRepository.save(verificationToken);
  }

  public VerificationToken getVerificationToken(String token){
    return  tokenRepository.findByToken(token);
  }

  public void verifyUser(String token){
    VerificationToken verificationToken = getVerificationToken(token);
    if(
          verificationToken != null // does exist
        &&
          verificationToken.getExpiryDate().isAfter(LocalDateTime.now())  // not expired
    ){
      User user = verificationToken.getUser();
      user.setVerified(true);
      userRepository.save(user);
     // tokenRepository.delete(verificationToken); // we might keep it for our future logs
    }
  }

  private void sendVerificationEmail(User user) throws MailException {
    String token = UUID.randomUUID().toString();
    createVerificationToken(user, token);

    String recipientAddress = user.getEmail();
    String subject = "Email Verification";
    String confirmationUrl = "http://localhost:8085/verify?token="+token;
    String message = "Please click the link below to verify your email address:\n"+ confirmationUrl;

    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(message);

    mailSender.send(email);

  }

}
