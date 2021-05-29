package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryDetailRes {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss aa")
    private Date bookingTime;

    private String deposit; //tien coc

    private String bookingStatus;

    private int totalSeats;

    private String tableName;

    private String point; //tien thuong ung voi tong hoa don

    private String moneyPay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date payDate;

    private String bookingId;

}
