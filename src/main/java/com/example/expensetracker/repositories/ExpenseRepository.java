package com.example.expensetracker.repositories;

import com.example.expensetracker.models.Expense;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.example.expensetracker.models.User;
import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE Expense  e SET e.amount=?1 WHERE e.id=?2")
  void updateExpenseAmount(Double amount, Long id);

  @Modifying
  @Transactional
  @Query("DELETE FROM Expense e WHERE e.category=?1")
  void deleteExpenseByCategory(String category);

  @Modifying
  @Transactional
  @Query("UPDATE Expense  e SET e.description=?1 WHERE e.id=?2")
  void updateExpenseDescription(String description, Long id);

  @Modifying
  @Transactional
  @Query("UPDATE Expense  e SET e.category=?1  WHERE e.id=?2")
  void updateExpenseCategory(String category, Long id);


  @Modifying
  @Transactional
  @Query("DELETE FROM Expense e WHERE e.date BETWEEN ?1 AND ?2")
  void deleteExpensesByDateRange(LocalDate startDate, LocalDate endDate);

  @Modifying
  @Transactional
  @Query("UPDATE Expense e SET e.date=?1 WHERE e.id=?2")
  void updateExpenseDate(LocalDate date, Long id);

  @Modifying
  @Transactional
  @Query("UPDATE Expense e SET e.user =?1 WHERE e.id=?2")
  void updateExpenseUser(User user, Long id);

  @Modifying
  @Transactional
  @Query("DELETE FROM Expense e WHERE e.user.id=?1")
  void deleteExpensesByUser(Long userId);

  @Modifying
  @Transactional
  @Query("UPDATE Expense e SET e.amount = ?1 WHERE e.id IN ?2")
  void updateMultipleExpenses(Double amount, List<Long> ids);

  @Modifying
  @Transactional
  @Query("DELETE FROM Expense e WHERE e.id = ?1")
  void deleteExpenseById(Long id);


}

/**
 * The ExpenseRepository  extends JpaRepository, so default CRUD methods are transactional.
 * The ExpenseService class uses @Transactional to manage transactions at the service layer
 *
 * */
