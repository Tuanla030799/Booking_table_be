package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryRes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss aa")
    private Date bookingTime;

    private String deposit;

    private String bookingStatus;

    private String moneyPay;

    private int stt;

    private String bookingId;

}
