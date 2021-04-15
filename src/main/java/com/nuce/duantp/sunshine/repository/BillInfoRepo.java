package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_BillInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillInfoRepo extends CrudRepository<tbl_BillInfo, Long> {
       tbl_BillInfo findByBillIdAndFoodId(String billId,Long foodId);
}

