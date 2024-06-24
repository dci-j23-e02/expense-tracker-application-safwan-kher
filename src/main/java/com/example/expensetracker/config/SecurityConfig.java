package com.example.expensetracker.config;

import com.example.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



  @Bean
  public UserDetailsService userDetailsService(UserService userService){
        return userService;
  }



  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/signup", "/login", "/verify", "/assign-admin").permitAll()
                .requestMatchers("/currency-converter").hasRole("ADMIN")
                .requestMatchers("/admin-home").hasRole("ADMIN")
                .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        )
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .invalidateHttpSession(true) // This is the default behavior
     //   .deleteCookies("JSESSIONID") // Optional, as session invalidation already removes the cookie
        .permitAll();


    return http.build();
  }

  @Autowired
  public void configureGlobal(
      AuthenticationManagerBuilder auth,
      UserDetailsService userDetailsService
  ) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
  }

}
