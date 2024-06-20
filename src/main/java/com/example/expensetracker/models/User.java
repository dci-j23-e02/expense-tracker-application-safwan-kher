package com.example.expensetracker.models;

import jakarta.persistence.*;
import java.util.Set;


@Entity
@Table(name ="users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Expense> expenses;

  @Column(nullable = false)
  private boolean verified=false;


  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name ="user_id"))
  @Column(name = "role")
  private Set<String> roles;

  public User() {
  }

  public User(Long id, String username, String email, String password,
      Set<Expense> expenses, boolean verified, Set<String> roles) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.expenses = expenses;
    this.verified = verified;
    this.roles = roles;
  }

  public User(String username, String email, String password,
      Set<Expense> expenses, boolean verified, Set<String> roles) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.expenses = expenses;
    this.verified = verified;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Expense> getExpenses() {
    return expenses;
  }

  public void setExpenses(Set<Expense> expenses) {
    this.expenses = expenses;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }
}
