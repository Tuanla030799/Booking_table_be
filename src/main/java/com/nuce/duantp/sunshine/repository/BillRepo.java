package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepo extends CrudRepository<tbl_Bill, Long> {
       tbl_Bill findByBookingId(String bookingId);
}

