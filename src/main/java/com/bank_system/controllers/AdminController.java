package com.bank_system.controllers;

import com.bank_system.services.AdminServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminServices adminServices;

    public AdminController(AdminServices adminServices) {
        this.adminServices = adminServices;
    }

    @GetMapping
    public String adminPage(Model model){
        HashMap<String,Object> data = adminServices.loadingDashboardData();
        model.addAttribute("totalUsers",data.get("numberOfUsers"));
        model.addAttribute("totalBalance",data.get("totalBalance"));
        model.addAttribute("thisMonthJoinedUsers",data.get("thisMonthJoinedUsers"));
        model.addAttribute("totalTransferAmount",data.get("totalTransferAmount"));
        model.addAttribute("totalTransferCount",data.get("totalTransferCount"));
        model.addAttribute("users",data.get("users"));
        return "admin";
    }
}
