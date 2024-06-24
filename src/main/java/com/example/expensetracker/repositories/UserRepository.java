package com.example.expensetracker.repositories;

import com.example.expensetracker.models.Role;
import com.example.expensetracker.models.User;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByEmail(String email);

  @Modifying
  @Query("UPDATE User u SET u.roles=?2 WHERE u.username=?1")
  void updateUserRoles(String username, Set<Role> roles);
}
