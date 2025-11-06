package com.bank_system.services;

import com.bank_system.models.AdminUser;
import com.bank_system.repositories.TransactionRepository;
import com.bank_system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminServices {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AdminServices(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
    public HashMap<String,Object> loadingDashboardData(){
        HashMap<String,Object> result = new HashMap<>();
        int numberOfUsers = userRepository.countUsers();
        double totalBalance = userRepository.sumUsersAmounts();
        int thisMonthJoinedUsers = userRepository.peopleJoinedThisMonth();
        double totalTransferCount = transactionRepository.getCompleteTransactions();
        double totalTransferAmount = transactionRepository.getCompleteTransactionsAmount();
        List<Object[]> users = userRepository.findAllUsers();
        List<AdminUser> adminUsers =ProcessUsers(users) ;
        result.put("numberOfUsers",numberOfUsers);
        result.put("totalBalance",totalBalance);
        result.put("thisMonthJoinedUsers",thisMonthJoinedUsers);
        result.put("totalTransferCount",totalTransferCount);
        result.put("totalTransferAmount",totalTransferAmount);
        result.put("users",adminUsers);
        return result;
    }
    private List<AdminUser> ProcessUsers(List<Object[]> users){
        List<AdminUser> adminUsers = new ArrayList<>();
         for(Object[] user:users){
             AdminUser adminUser = new AdminUser(user[2].toString(),
                     user[1].toString(),
                     user[3].toString(),
                     user[4].toString(),
                     user[5].toString(),
                     user[0].toString(),
                     user[6].toString(),
                     user[7].toString(),
                     user[8].toString());
             adminUsers.add(adminUser);
         }
         return adminUsers;
    }
}
