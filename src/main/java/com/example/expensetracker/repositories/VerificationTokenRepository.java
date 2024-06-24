package com.example.expensetracker.repositories;

import com.example.expensetracker.models.User;
import com.example.expensetracker.models.VerificationToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  VerificationToken findByToken(String token);



 @Query("SELECT v FROM  VerificationToken v WHERE v.user.username=?1")
  VerificationToken findByUsername(String username);
}
