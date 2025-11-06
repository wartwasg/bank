package com.bank_system.services;

import com.bank_system.models.Transaction;
import com.bank_system.models.User;
import com.bank_system.repositories.TransactionRepository;
import com.bank_system.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class ProcessTransactions {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ProcessTransactions(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }
    public void save(Transaction transaction, User user) {

        transaction.setS_account(user.getAccountNumber());
        transaction.setType("transfer");
        transaction.setUser(user);
        transactionRepository.save(transaction);

        User receiver = userRepository.findUserByAccountNumber(transaction.getR_account());

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setDate(transaction.getDate());
        newTransaction.setUser(receiver);
        newTransaction.setType("deposit");
        newTransaction.setDescription(transaction.getDescription());
        newTransaction.setR_account(transaction.getR_account());
        newTransaction.setRecepient(transaction.getRecepient());
        newTransaction.setS_account(transaction.getS_account());

        transactionRepository.save(newTransaction);

        userRepository.updateSenderBalance(transaction.getS_account(), transaction.getAmount());
        userRepository.updateReceiverBalance(transaction.getR_account(), transaction.getAmount());
    }

    public List<Transaction> getTransaction(String account, String type) {
        List<Transaction> transactions = transactionRepository.getTransactions(account, type);
        return transactions.isEmpty() ? null : transactions;
    }

    public HashMap<String,List<Double>> getUSerExpenditureAndIncome(String account, String type1, String type2,String type3) {
        HashMap<String,List<Double>> status = new HashMap<>();
        List<Object[]> expensesList = transactionRepository.getMonthlyTransactionsIncome(account, type1, type2);
        List<Object[]> incomeList = transactionRepository.getMonthlyTransactionsExpenses(account, type3);
        List<Double> expenditure = new ArrayList<>(Collections.nCopies(12, 0.00));
        List<Double> incomeOf = new ArrayList<>(Collections.nCopies(12, 0.00));
        for (Object[] obj : expensesList) {
            if (obj[1] != null) {
                expenditure.set(((Number)obj[0]).intValue()-1,((Number)obj[1]).doubleValue());
            }
        }
        for (Object[] obj : incomeList) {
            if (obj[1] != null) {
                incomeOf.set(((Number)obj[0]).intValue()-1,((Number)obj[1]).doubleValue());
            }
        }
        status.put("income",incomeOf);
        status.put("expenditure",expenditure);
        return status;
    }
    public List<Transaction> getPastTransactions(User user) {
        List<Transaction> historyOfTransactions = transactionRepository.getTransactionsHistory(user.getId());
        return historyOfTransactions.isEmpty() ? null : historyOfTransactions;
    }
}
