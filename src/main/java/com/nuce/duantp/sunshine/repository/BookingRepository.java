package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<tbl_Booking, String> {
    @Override
    List<tbl_Booking> findAll();

    @Query("SELECT u FROM tbl_Booking u where u.bookingStatus != 0 ")
    List<tbl_Booking> getAll();

    List<tbl_Booking> findByBookingStatusAndTableNameAndBookingTimeBetween(int status, String tableName, Date timeStart, Date timeEnd);

    List<tbl_Booking> findAllByEmailAndBookingStatus(String email, int status);

//    List<tbl_Booking> findAllByEmailOrderByBookingTimeDesc(String email);

    @Query("SELECT u FROM tbl_Booking u WHERE u.email =:email order by u.bookingStatus asc ,u.bookingTime desc")
    List<tbl_Booking> getListBookingByEmail(@Param("email") String email);

    tbl_Booking findByBookingId(String bookingId);

    List<tbl_Booking> findByBookingStatusAndConfirmBookingAndBookingTimeLessThan(int status,int cf, Date bookingTime);

    //    List<tbl_Booking> findAllByOrderByBookingStatusDesc();
    @Query("SELECT u FROM tbl_Booking u order by u.bookingStatus asc ,u.bookingTime desc")
    List<tbl_Booking> getListBookingAdmin();
}
