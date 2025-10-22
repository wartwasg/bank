package com.bank_system.controllers;

import com.bank_system.models.RegistrationModel;
import com.bank_system.services.LoginAndRegisterService;
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
@RequestMapping("/register")
public class RegisterController {

    private final LoginAndRegisterService loginAndRegisterService;

    public RegisterController(LoginAndRegisterService loginAndRegisterService) {
        this.loginAndRegisterService = loginAndRegisterService;
    }

    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("user", new RegistrationModel());
        return "register";
    }
    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") RegistrationModel user, Errors errors, Model model, RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            model.addAttribute("error",errors);
            return "register";
        }
        else {
            boolean status = loginAndRegisterService.UserEmailExists(user.getEmail());
            if (status) {
                model.addAttribute("user_exists","Email already exists");
                return "register";
            }
            else  {
                if(user.getPassword().equals(user.getConfirmPassword())) {
                    loginAndRegisterService.CreateUser(user);
                    redirectAttributes.addFlashAttribute("success", "User has been registered successfully");
                    return "redirect:/login";
                }else {
                    model.addAttribute("fail","Passwords do not match");
                    return "register";
                }

            }
        }

    }
}
