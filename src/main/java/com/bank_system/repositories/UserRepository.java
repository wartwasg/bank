package com.bank_system.repositories;

import com.bank_system.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);
    User findUserByAccountNumber(String accountNumber);
    @Transactional
    @Modifying
    @Query(value ="update users set balance = balance - :transferred where account_number=:number",nativeQuery = true)
    void updateSenderBalance(@Param("number") String accountNumber, @Param("transferred") double transferred);
    @Transactional
    @Modifying
    @Query(value ="update users set balance = balance + :transferred where account_number=:number",nativeQuery = true)
    void updateReceiverBalance(@Param("number") String accountNumber, @Param("transferred") double transferred);
}
