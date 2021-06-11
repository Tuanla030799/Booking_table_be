package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

//
//    public BookingHistoryRes(tbl_Booking booking) {
//
//    }
}
