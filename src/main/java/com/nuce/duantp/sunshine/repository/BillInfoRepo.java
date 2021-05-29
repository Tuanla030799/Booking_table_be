package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_BillInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillInfoRepo extends CrudRepository<tbl_BillInfo, Long> {
       tbl_BillInfo findByBillIdAndFoodId(String billId,Long foodId);
       List<tbl_BillInfo> findAllByBillId(String billId);
}

