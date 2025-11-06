package com.bank_system.controllers;

import com.bank_system.models.Deposits;
import com.bank_system.models.User;
import com.bank_system.services.ProcessDeposits;
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

@Controller
@RequestMapping("deposit")
public class DepositController {

    private final ProcessDeposits processDeposits;

    public DepositController(ProcessDeposits processDeposits) {
        this.processDeposits = processDeposits;
    }

    @GetMapping
    public String depositPage(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("balance",user.getBalance());
        model.addAttribute("deposit",new Deposits());
        model.addAttribute("user",user);
        return "deposit";
    }
    @PostMapping
    public String deposit(@Valid @ModelAttribute("deposit") Deposits deposits, Errors errors, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        User user = (User) session.getAttribute("user");
        model.addAttribute("balance",user.getBalance());
        if(errors.hasErrors() || deposits.getDepositedAmount() <= 0){
            model.addAttribute("errors",errors);
            return "deposit";
        }else{
            try{
                processDeposits.saveDeposits(deposits,user);
                redirectAttributes.addFlashAttribute("successMessage","the deposit has been sent successfully");
                return "redirect:/dashboard";
            }
            catch(Exception e){
                System.out.println("I reach here and there is this problem :"+e);
                return "deposit";
            }
        }
    }
}
