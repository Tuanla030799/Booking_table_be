package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepo extends CrudRepository<tbl_Bill, Long> {
//       tbl_Bill findAllByBookingIdAndDiscountGreaterThan(String bookingId,Long discount);
       tbl_Bill findByBookingId(String bookingId);
//       tbl_Bill findByBillId(String billId);
}

