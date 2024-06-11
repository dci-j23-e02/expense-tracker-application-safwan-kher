package com.example.expensetracker.service;


import com.example.expensetracker.models.Expense;
import com.example.expensetracker.repositories.ExpenseRepository;
import jakarta.transaction.Transactional;
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




}
