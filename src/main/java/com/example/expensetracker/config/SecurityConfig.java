package com.example.expensetracker.config;

import com.example.expensetracker.service.UserService;
import jakarta.annotation.PostConstruct;
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

  @Autowired
  private CustomLogoutSuccessHandler customLogoutSuccessHandler;

  @Bean
  public UserDetailsService userDetailsService(UserService userService){
        return userService;
  }



  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/signup", "/login").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessHandler(customLogoutSuccessHandler)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll()
        )
        .headers(headers -> headers
            .cacheControl(cache -> cache.disable())
        )
        .sessionManagement(sessionManagement -> sessionManagement
            .invalidSessionUrl("/login?invalid-session=true")
        );

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
