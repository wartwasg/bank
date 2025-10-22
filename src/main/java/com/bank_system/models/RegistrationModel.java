package com.bank_system.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationModel {
    private String username;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+",message = "invalid first name")
    private String fname;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+",message = "invalid surname name")
    private String lname;
    @NotBlank
    @Pattern(regexp = "^(\\+2557[0-9]{8}|\\+2556[0-9]{8})$",message = "invalid phone number")
    private String phone;
    @Size(min = 8, max = 20,message = "password should not be less than 8 characters")
    private String password;
    private String confirmPassword;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email")
    private String email;
    @Pattern(regexp = "personal|business|savings")
    private String accountType;
    private String role;
    private double balance;
    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(fname+" "+lname,fname,lname,passwordEncoder.encode(password),email,phone,"USER",accountType,0.00);
    }
}
