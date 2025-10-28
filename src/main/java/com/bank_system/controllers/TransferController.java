package com.bank_system.controllers;

import com.bank_system.models.Transaction;
import com.bank_system.models.User;
import com.bank_system.services.ProcessTransactions;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    private final ProcessTransactions processTransactions;

    public TransferController(ProcessTransactions processTransactions) {
        this.processTransactions = processTransactions;
    }

    @GetMapping
    public String transfer(HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        Transaction transaction = (Transaction)session.getAttribute("transaction");
        List<Transaction> transactions = processTransactions.getTransaction(user.getAccountNumber(),"transfer");
        model.addAttribute("transactions",transactions);
        if(transaction!=null){
            model.addAttribute("transaction",transaction);
        }
        else {
            session.setAttribute("transaction",new Transaction());
            model.addAttribute("transaction",new Transaction());
        }
        model.addAttribute("balance", user.getBalance());
        return "transfer";
    }
    @PostMapping
    public String processTransfer(@Valid @ModelAttribute("transaction") Transaction transaction, Errors errors, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        User user = (User)session.getAttribute("user");
        model.addAttribute("balance",user.getBalance());
        if (errors.hasErrors()) {
            model.addAttribute("errors",errors);
            return "transfer";
        }
        if((transaction!=null) && (transaction.getAmount()>0) &&
                (transaction.getAmount()<user.getBalance()) && !(transaction.getR_account().trim().equals(user.getAccountNumber().trim()))){
            processTransactions.save(transaction,user);
            redirectAttributes.addFlashAttribute("successMessage","Transfer completed successfully!");
            return "redirect:/dashboard";
        }
        else{
            model.addAttribute("errorMessage","Transfer failed: please check the amount, balance, and recipient account.");
            return "transfer";
        }

    }
}
