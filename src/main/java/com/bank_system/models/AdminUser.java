package com.bank_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUser {
    private String username;
    private String balance;
    private String phone;
    private String role;
    private String created_date;
    private String account_number;
    private String fname;
    private String lname;
    private String email;
}
