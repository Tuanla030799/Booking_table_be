package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<tbl_Booking, Long> {
    @Override
    List<tbl_Booking> findAll();

    @Query(nativeQuery = true, value = "exec pr_Booking @Email =:Email,@BookingTime=:BookingTime," +
            "@AccountNo=:AccountNo,@TotalSeats =:TotalSeats,@TableName=:tableName")
    String bookingTable(@Param("Email") String Email, @Param("BookingTime") String BookingTime,
                        @Param("AccountNo") String AccountNo, @Param("TotalSeats") int TotalSeats,
                        @Param("tableName") String tableName);

    @Query(nativeQuery = true, value = "exec pr_Order @Email =:Email,@BookingId =:BookingId,@BillId =:BillId," +
            "@Quantity =:Quantity,@FoodID =:FoodID")
    String orderFood(@Param("Email") String Email, @Param("BookingId") String BookingId,
                     @Param("BillId") String BillId, @Param("Quantity") int Quantity, @Param("FoodID") Long FoodID);

    @Query(nativeQuery = true, value = "exec pr_Pay @email =:email, @accountNo =:accountNo,@bookingId =:bookingId," +
            "@discount =:discount")
    String pay(@Param("email") String email, @Param("accountNo") String accountNo,
               @Param("bookingId") String bookingId, @Param("discount") Long discount);

    @Query(nativeQuery = true, value = "exec pr_CancelBooking @email =:email,@bookingId =:bookingId")
    String cancelBooking(@Param("email") String email, @Param("bookingId") String bookingId);

    @Query(nativeQuery = true, value = "exec pr_CancelBookingAdmin @bookingId =:bookingId")
    String cancelBookingAdmin(@Param("bookingId") String bookingId);

    List<tbl_Booking> findByBookingStatusAndTableNameAndBookingTimeBetween(int status,String tableName,
                                                                              Date timeStart,Date timeEnd);
    List<tbl_Booking> findAllByEmailAndBookingStatus(String email, int status);

    List<tbl_Booking> findByEmail(String email);
}
