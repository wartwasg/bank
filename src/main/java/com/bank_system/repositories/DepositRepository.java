package com.bank_system.repositories;

import com.bank_system.models.Deposits;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposits,Integer> {
    @Transactional
    @Modifying
    @Query(value = "update users "+
                   "set balance = balance + :depositedAmount "+
                   "where account_number = :UserNumber; "+
                   "update users "+
                   "set balance = balance - :depositedAmount "+
                   "where account_number = :agentNumber;"
            ,nativeQuery = true)
    void processRequestedPayment(@Param("depositedAmount") Double depositedAmount, @Param("agentNumber") String agentNumber, @Param("UserNumber") String UserNumber);
}
