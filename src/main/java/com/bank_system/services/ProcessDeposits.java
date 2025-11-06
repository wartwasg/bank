package com.bank_system.services;

import com.bank_system.models.Deposits;
import com.bank_system.models.Transaction;
import com.bank_system.models.User;
import com.bank_system.repositories.DepositRepository;
import com.bank_system.repositories.TransactionRepository;
import com.bank_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProcessDeposits {

    private final DepositRepository depositRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public ProcessDeposits(DepositRepository depositRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.depositRepository = depositRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
    @Transactional
    public void saveDeposits(Deposits deposits, User user){
        depositRepository.processRequestedPayment(deposits.getDepositedAmount(), user.getAccountNumber(), deposits.getDepositedAccount());
        depositRepository.save(deposits);
        HashMap<String,Transaction> transactionsPerUser = getTransaction(deposits,user);
        transactionRepository.saveAll(transactionsPerUser.values());
    }
    public HashMap<String,Transaction> getTransaction(Deposits deposits, User user){
        HashMap<String,Transaction>  transactionForSave = new HashMap<>();
        try{
            User receiver = userRepository.findUserByAccountNumber(deposits.getDepositedAccount());
            transactionForSave.put("Agent",getTransactionForUser(deposits,user,"transfer",receiver,"agent"));
            transactionForSave.put("User",getTransactionForUser(deposits,user,"deposit",receiver,"user"));
        }
        catch(Exception e){
            System.out.println("Error in processing deposit the user not found :"+e.getMessage());
        }
        return transactionForSave;
    }
    public Transaction getTransactionForUser(Deposits deposits,User user,String type,User reciever,String userType){
        Transaction trans = new Transaction();
        trans.setAmount(deposits.getDepositedAmount());
        trans.setR_account(deposits.getDepositedAccount());
        trans.setDate(deposits.getDate());
        trans.setS_account(user.getAccountNumber());
        if (userType.equals("agent")){
            trans.setDescription("you have sent a commission to "+reciever.getUsername());
            trans.setUser(user);
        }
        else if (userType.equals("user")){
            trans.setDescription("you have received a transaction from "+user.getUsername());
            trans.setUser(reciever);
        }
        trans.setRecepient(deposits.getCardHolderName());
        trans.setType(type);
        return trans;
    }
}
