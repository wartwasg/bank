package com.bank_system.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String fname;
    private String lname;
    private String phone;
    private String accountType;
    @Column(unique = true)
    private String accountNumber;
    private String password;
    @Column(unique = true)
    private String email;
    private String role;
    private Date createdDate = new Date();
    private double balance;

    public User(String username,String fname,String lname, String password, String email,String phone, String role,String accountType,String AccountNumber, double balance) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.accountType = accountType;
        this.accountNumber = AccountNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Transaction> transactions = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Deposits> deposits = new ArrayList<>();
}
