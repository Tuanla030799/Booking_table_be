package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_Table;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepo extends CrudRepository<tbl_Table, Long> {
        List<tbl_Table> findBySeatGreaterThanEqualOrderBySeatAsc(int seat);
//        List<tbl_Table> findBySeatLessThanOrderBySeatDesc(int seat);
}

