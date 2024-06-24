package com.example.expensetracker.models;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column( nullable = false)
  private Long id;


  @Column(unique = true, nullable = false)
  private String name;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users;


  public Role() {
  }

  public Role(Long id, String name, Set<User> users) {
    this.id = id;
    this.name = name;
    this.users = users;
  }

  public Role(String name, Set<User> users) {
    this.name = name;
    this.users = users;
  }




  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}