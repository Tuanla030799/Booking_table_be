package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.model.tbl_Booking;
import com.nuce.duantp.sunshine.model.tbl_Deposit;
import com.nuce.duantp.sunshine.repository.DepositRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryRes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss aa")
    private Date bookingTime;

    private Long deposit;

    private String bookingStatus;

    private float moneyPay;

    private int stt;

    private String bookingId;
//    public BookingHistoryRes(tbl_Booking booking, float moneyPay,int stt) {
//        this.bookingTime = booking.getBookingTime();
//        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
//        this.deposit = deposit.getDeposit();
//        this.bookingStatus = booking.getBookingStatus() == 1 ? "Đã thanh toán!" : "Chưa thanh toán!";
//        this.moneyPay = moneyPay;
//        this.stt=stt;
//    }
}
