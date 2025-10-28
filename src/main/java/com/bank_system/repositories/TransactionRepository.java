package com.bank_system.repositories;

import com.bank_system.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    @Query(value = "SELECT COALESCE(SUM(t.amount), 0) FROM transaction t JOIN users u ON t.user_id = u.id WHERE u.email = :email AND t.type = :type", nativeQuery = true)
    double getAmountIncome(@Param("email") String email, @Param("type") String type);
    @Query(value = "select * from transaction t join users u on t.user_id=u.id where t.s_account = :account AND t.type = :type order by t.date DESC limit 6",nativeQuery = true)
    List<Transaction> getTransactions(@Param("account") String account, @Param("type") String type);
    @Query(value = "select extract(month from t.date) as month,sum(t.amount) from transaction t join users u ON t.user_id = u.id where t.s_account = :account AND (t.type = :type1 OR t.type=:type2) group by month ",nativeQuery = true)
    List<Object[]> getMonthlyTransactionsIncome(@Param("account") String account, @Param("type1") String type1,@Param("type2") String type2);
    @Query(value = "select extract(month from t.date) as month,sum(t.amount) from transaction t join users u ON t.user_id = u.id where t.r_account = :account AND (t.type = :type1) group by month ",nativeQuery = true)
    List<Object[]> getMonthlyTransactionsExpenses(@Param("account") String account, @Param("type1") String type1);
    @Query(value = "select * from transaction t join users u on t.user_id=u.id where t.s_account = :account1 OR t.r_account = :account2 order by t.date DESC limit 5",nativeQuery = true)
    List<Transaction> getTransactionsHistory(@Param("account1") String SenderAccount, @Param("account2") String ReceiverAccount);
}
