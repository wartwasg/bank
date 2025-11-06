package com.bank_system.repositories;

import com.bank_system.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);

    User findUserByAccountNumber(String accountNumber);

    @Transactional
    @Modifying
    @Query(value ="update users set balance = balance - :transferred where account_number=:number",nativeQuery = true)
    void updateSenderBalance(@Param("number") String accountNumber, @Param("transferred") Double transferred);

    @Transactional
    @Modifying
    @Query(value ="update users set balance = balance + :transferred where account_number=:number",nativeQuery = true)
    void updateReceiverBalance(@Param("number") String accountNumber, @Param("transferred") Double transferred);

    @Query(value = "select count(username) from users",nativeQuery = true)
    Integer countUsers();

    @Query(value = "select sum(balance) from users",nativeQuery = true)
    Double sumUsersAmounts();

    @Query(value = "select count(username) from users where extract(month from created_date) = extract(month from now()) ",nativeQuery = true)
    int peopleJoinedThisMonth();

    @Query(value = "select account_number,balance,username,phone,role,created_date,fname,lname,email from users",nativeQuery = true)
    List<Object[]> findAllUsers();
}
