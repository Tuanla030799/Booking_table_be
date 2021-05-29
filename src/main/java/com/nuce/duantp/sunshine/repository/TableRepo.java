package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepo extends CrudRepository<tbl_Table, Long> {
        List<tbl_Table> findBySeatGreaterThanEqualOrderBySeatAsc(int seat);
}

