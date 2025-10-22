package com.bank_system.services;

import com.bank_system.models.RegistrationModel;
import com.bank_system.models.User;
import com.bank_system.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginAndRegisterService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    public LoginAndRegisterService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
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
}
