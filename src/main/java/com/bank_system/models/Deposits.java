package com.bank_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.Date;

@Data
@Entity
public class Deposits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double depositedAmount;
    @CreditCardNumber(message = "invalid card number")
    private String depositedAccount;
    @Pattern(regexp = "[a-zA-Z ]+",message = "invalid cardholder Name")
    private String cardHolderName;
    @Pattern(regexp = "^(0[1-9]/[0-9]{2}|1[0-2]/[0-9]{2})",message = "invalid expire date")
    private String expireDate;
    @Pattern(regexp = "[0-9]{3}",message = "invalid cvv number")
    private String cvv;
    private String description;
    private Date date = new Date();
    @ManyToOne
    @JoinColumn(name = "user_deposited")
    private User user;
}
