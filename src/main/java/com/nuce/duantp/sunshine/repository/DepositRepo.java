package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Deposit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepo extends CrudRepository<tbl_Deposit, Long> {
       List<tbl_Deposit>  findAllByTotalPersons(int totalPerson);
       tbl_Deposit findByDepositId(Long depositId);
//       @Query("select top(1) e.DepositId from tbl_deposit e where e.TotalPersons >= totalSeats order by e" +
//               ".TotalPersons " +
//               "asc, e.Created desc")
//       tbl_Deposit findByNameAndAddress(@Param("name") String name, @Param("address") String address);
       tbl_Deposit findTopByTotalPersonsLessThanEqualOrderByTotalPersonsAscCreatedDesc(int totalPerson);
}

