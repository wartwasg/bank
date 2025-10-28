package com.bank_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.Date;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ids;
    private String s_account;
    @CreditCardNumber(message = "invalid account number")
    private String r_account;
    @Pattern(regexp = "[a-zA-z ]+",message = "invalid recepient name")
    @Size(min = 4, max = 20,message = "invalid recepient name")
    private String recepient;
    private String type;
    private double amount;
    private String description;
    private Date date = new Date();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
