package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryRes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss aa")
    private Date bookingTime;

    private String deposit;

    private String bookingStatus;

    private String moneyPay;

    private int stt;

    private String bookingId;

    private String refund;

    private String email;

    private String phoneNumber;

    List<ListFoodInBooking> listFoodInBookings;

    public BookingHistoryRes(Date bookingTime, String deposit, String bookingStatus, String moneyPay, int stt,
                             String bookingId, String refund,List<ListFoodInBooking> listFoodInBookings) {
        this.bookingTime = bookingTime;
        this.deposit = deposit;
        this.bookingStatus = bookingStatus;
        this.moneyPay = moneyPay;
        this.stt = stt;
        this.bookingId = bookingId;
        this.refund = refund;
        this.listFoodInBookings=listFoodInBookings;
    }
//
//    public BookingHistoryRes(tbl_Booking booking) {
//
//    }
}
