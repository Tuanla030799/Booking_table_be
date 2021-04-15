package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends CrudRepository<tbl_BankAccount, Long> {
        List<tbl_BankAccount> findByEmail(String email);
}

