package com.bank_system.services;

import com.bank_system.models.RegistrationModel;
import com.bank_system.models.User;
import com.bank_system.repositories.TransactionRepository;
import com.bank_system.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class LoginAndRegisterService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;


    public LoginAndRegisterService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public boolean UserEmailExists(String email) {
       User user = userRepository.findUserByEmail(email);
       if(user==null)return false;
       return true;
    }
    public boolean CreateUser(RegistrationModel user){
        User regUser = user.toUser(bCryptPasswordEncoder);
        userRepository.save(regUser);
        return true;
    }
    public String getCurrentUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(principal instanceof User){
                return ((User)principal).getEmail();
            }
            else if(principal instanceof String username){
                return username;
            }
        }
        return null;
    }
    public User getUserData(String email) {
        User user = userRepository.findUserByEmail(email);
        return user;
    }
    public HashMap<String,Double> getExpenditure(String email){
        HashMap<String,Double> usage = new HashMap<>();
        Double expenses = transactionRepository.getAmountIncome(email,"withdraw")+ transactionRepository.getAmountIncome(email,"transfer");
        usage.put("income",transactionRepository.getAmountIncome(email,"deposit"));
        usage.put("expenses",expenses);
        return usage;
    }

}
