package com.example.expensetracker.controllers;


import com.example.expensetracker.models.Expense;
import com.example.expensetracker.models.User;
import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.service.UserService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal; // Principal: refers to the currently logged-in user
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

  @Autowired
  private ExpenseService expenseService;

  @Autowired
  private UserService userService;

  @GetMapping
  public String listExpenses(Model model, Principal principal ){
    User user = userService.findByUsername(principal.getName());
    List<Expense> expenses = expenseService.findAllExpensesByUser(user.getId());
    model.addAttribute("expenses", expenses);
    return "expenses";
  }

  @GetMapping("/add")
  public String addExpenseForm(Model model){
    model.addAttribute("expense", new Expense());
    return "add-expense";
  }

  @PostMapping("/add")
  public String addExpense(Expense expense,  Principal principal){
    User user  = userService.findByUsername(principal.getName());
    expense.setUser(user);
    expenseService.saveExpense(expense);
    return "redirect:/expenses";
  }

  @PostMapping("/updateAmount")
  public String updateExpenseAmount(
      @RequestParam Double amount,
      @RequestParam Long id
  ){
    expenseService.updateExpenseAmount(amount, id);
    return "redirect:/expenses";
  }

  @PostMapping("/deleteByCategory")
  public  String deleteExpenseByCategory(
      @RequestParam String category
  ){
    expenseService.deleteExpenseByCategory(category);
    return "redirect:/expenses";
  }

  @PostMapping("/updateDescription")
  public String updateExpenseDescription(
      @RequestParam String description,
      @RequestParam Long id
  ){
    expenseService.updateExpenseDescription(description, id);
    return "redirect:/expenses";
  }

  @PostMapping("/updateCategory")
  public String updateExpenseCategory(
      @RequestParam String category,
      @RequestParam Long id
  ){
    expenseService.updateExpenseCategory(category, id);
    return "redirect:/expenses";
  }

  @PostMapping("/deleteByDateRange")
  public String deleteExpensesByDAteRange(
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate,
      Principal principal
  ){
    User user = userService.findByUsername(principal.getName());
    expenseService.deleteExpensesByDateRangeAndUser(startDate, endDate, user.getId());
    return "redirect:/expenses";
  }


  @PostMapping("/updateDate")
  public String updateExpenseDate(
      @RequestParam LocalDate date,
      @RequestParam Long id
  ){
    expenseService.updateExpenseDate(date, id);
    return "redirect:/expenses";
  }


  @PostMapping("/deleteByUser")
  public String deleteExpenseByUser(
      @RequestParam Long userId

  ) {
        expenseService.deleteExpenseByUser(userId);
        return "redirect:/expenses";
  }

  @PostMapping("/updateMultiple")
  public String updateMultipleExpenses(
      @RequestParam Double amount,
      @RequestParam List<Long> ids
      ){
    expenseService.updateMultipleExpenses(amount, ids);
    return "redirect:/expenses";

  }

  @PostMapping("/deleteById")
  public String deleteExpenseById(
      @RequestParam Long id
  ){
    expenseService.deleteExpenseById(id);
    return "redirect:/expenses";
  }




}
