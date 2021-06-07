package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<tbl_Booking, String> {
    @Override
    List<tbl_Booking> findAll();

    List<tbl_Booking> findByBookingStatusAndTableNameAndBookingTimeBetween(int status, String tableName, Date timeStart, Date timeEnd);

    List<tbl_Booking> findAllByEmailAndBookingStatus(String email, int status);

    List<tbl_Booking> findAllByEmailOrderByBookingTimeDesc(String email);

    tbl_Booking findByBookingId(String bookingId);

    List<tbl_Booking> findByBookingStatusAndBookingTimeLessThan(int status,Date bookingTime);

    List<tbl_Booking> findAllByOrderByBookingStatusDesc();

}
