package com.example.expensetracker.controllers;


import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ExpenseController {

  @Autowired
  private ExpenseService expenseService;

  @Autowired
  private UserService userService;







}
