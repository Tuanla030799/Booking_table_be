package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_BankAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends CrudRepository<tbl_BankAccount, Long> {
        List<tbl_BankAccount> findByEmail(String email);
        tbl_BankAccount findByAccountNo(String acc);
//        @Query(value = "select b from tbl_bankaccount where e.email:=email", nativeQuery = true)
//        tbl_BankAccount getBankAcc(@Param("email") String email);
}

