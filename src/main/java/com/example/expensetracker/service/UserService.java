package com.example.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
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


  @Transactional
  public void saveUser(User user){
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  public User findByUsername(String username){
    return userRepository.findByUsername(username);
  }


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // find our user in the database :
    User user = userRepository.findByUsername(username);
    /** O(1) <-
     * findByUsername(username); :1
     * assignment: 1
     * userRepository.findByUsername(username); :1
     * return : 1
     * -------------------
     * O(1) <-
     * userRepository.findByUsername(username); 1
     * */

    if(user == null){
      throw new UsernameNotFoundException("User not found");
    }


    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.emptyList()
    );
  }
}
