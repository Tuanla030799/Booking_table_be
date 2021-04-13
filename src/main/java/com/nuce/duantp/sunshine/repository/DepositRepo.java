package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Deposit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepo extends CrudRepository<tbl_Deposit, Long> {

       List<tbl_Deposit>  findAllByTotalPersons(int totalPerson);
}

