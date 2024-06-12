package com.example.expensetracker.service;


import com.example.expensetracker.models.Expense;
import com.example.expensetracker.models.User;
import com.example.expensetracker.repositories.ExpenseRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

  @Autowired
  private ExpenseRepository expenseRepository;

  @Transactional
  public Expense saveExpense(Expense expense) {
    if(expense == null){
      throw new IllegalArgumentException("invalid expense object");
    }
    return  expenseRepository.save(expense);
  }

  public List<Expense> findAllExpenses(){
    return expenseRepository.findAll();
  }


  // Implement other methods for custom operations

  @Transactional
  public void updateExpenseAmount(Double amount, Long id){
      expenseRepository.updateExpenseAmount(amount, id);
  }


  @Transactional
  public void deleteExpenseByCategory(String category){
    expenseRepository.deleteExpenseByCategory(category);
  }

  @Transactional
  public void updateExpenseDescription(String description, Long id){
    expenseRepository.updateExpenseDescription(description, id);
  }

  @Transactional
  public void updateExpenseCategory(String category, Long id){
      expenseRepository.updateExpenseCategory(category, id);
  }

  @Transactional
  public void deleteExpenseByDateRange(LocalDate startDate, LocalDate endDate){
    // validate : startDate is before endDate (optionally)
    expenseRepository.deleteExpensesByDateRange(startDate, endDate);
  }

  @Transactional
  public void updateExpenseDate(LocalDate date, Long id){
    expenseRepository.updateExpenseDate(date, id);
  }

  @Transactional
  public void deleteExpenseByUser(Long userId){
    expenseRepository.deleteExpensesByUser(userId);
  }

  @Transactional
  public void updateExpenseUser(User user, Long id){
    expenseRepository.updateExpenseUser(user, id);
  }

  @Transactional
  public void updateMultipleExpenses(Double amount, List<Long> ids){
    expenseRepository.updateMultipleExpenses(amount, ids);
  }

  @Transactional
  public void deleteExpenseById(Long id){
    expenseRepository.deleteExpenseById(id);
  }








}
