package com.bank_system.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationModel {
    private String username;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z ]+",message = "invalid first name")
    private String fname;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z ]+",message = "invalid surname name")
    private String lname;
    @NotBlank
    @Pattern(regexp = "^(\\+2557[0-9]{8}|\\+2556[0-9 ]{8})$",message = "invalid phone number")
    private String phone;
    @Size(min = 8, max = 20,message = "password should not be less than 8 characters")
    private String password;
    private String accountNumber;
    private String confirmPassword;
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email")
    private String email;
    @Pattern(regexp = "personal|business|savings")
    private String accountType;
    private String role;
    private double balance;
    public User toUser(PasswordEncoder passwordEncoder) {
        String account = generateAccountNumber();
        return new User(fname+" "+lname,fname,lname,passwordEncoder.encode(password),email,phone,"USER",accountType,account,0.00);
    }
    public static String generateAccountNumber() {
        Random random = new Random();
        String base = String.format("%010d", Math.abs(random.nextLong()) % 1_000_000_0000L);
        return base + luhn(base);
    }

    private static int luhn(String num) {
        int sum = 0;
        boolean alt = true;
        for (int i = num.length() - 1; i >= 0; i--) {
            int n = num.charAt(i) - '0';
            if (alt) n *= 2;
            sum += n / 10 + n % 10;
            alt = !alt;
        }
        int mod = sum % 10;
        return (10 - mod) % 10;
    }
}
