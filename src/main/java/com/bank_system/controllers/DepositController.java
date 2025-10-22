package com.bank_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("deposit")
public class DepositController {
    @GetMapping
    public String depositPage(){
        return "deposit";
    }
}
