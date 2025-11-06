package com.bank_system.controllers;

import com.bank_system.models.Transaction;
import com.bank_system.models.User;
import com.bank_system.services.LoginAndRegisterService;
import com.bank_system.services.ProcessTransactions;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final LoginAndRegisterService loginAndRegisterService;
    private final ProcessTransactions processTransactions;

    public DashboardController(LoginAndRegisterService loginAndRegisterService, ProcessTransactions processTransactions){
        this.loginAndRegisterService = loginAndRegisterService;
        this.processTransactions = processTransactions;
    }
    @GetMapping
    public String dashboard(Model model, HttpSession session){
        String email = loginAndRegisterService.getCurrentUserEmail();
        if(email != null){
           User user = loginAndRegisterService.getUserData(email);
           session.setAttribute("user",user);
           HashMap<String,List<Double>> MonthlyData = processTransactions.getUSerExpenditureAndIncome(user.getAccountNumber(),"withdraw","transfer","deposit");
           HashMap<String,Double> expenditure = loginAndRegisterService.getExpenditure(email);
           List<Transaction> userTransactions = processTransactions.getPastTransactions(user);
           model.addAttribute("username",user.getUsername());
           model.addAttribute("email",user.getEmail());
           model.addAttribute("avatar",String.valueOf(user.getFname().charAt(0))+String.valueOf(user.getLname().charAt(0)));
           model.addAttribute("balance",user.getBalance());
           model.addAttribute("account",user.getAccountNumber());
           model.addAttribute("income",expenditure.get("income"));
           model.addAttribute("expenditure",expenditure.get("expenses"));
           model.addAttribute("expensesList",MonthlyData.get("expenditure"));
           model.addAttribute("incomesList",MonthlyData.get("income"));
           model.addAttribute("transactions",userTransactions);
        }
        return "dashboard";
    }
}
