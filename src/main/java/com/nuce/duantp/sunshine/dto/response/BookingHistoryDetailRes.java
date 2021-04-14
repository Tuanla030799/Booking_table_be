package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.model.tbl_Bill;
import com.nuce.duantp.sunshine.model.tbl_Booking;
import com.nuce.duantp.sunshine.model.tbl_Deposit;
import com.nuce.duantp.sunshine.model.tbl_Points;
import com.nuce.duantp.sunshine.repository.DepositRepo;
import com.nuce.duantp.sunshine.repository.PointsRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryDetailRes {
    @Autowired
    DepositRepo depositRepo;
    @Autowired
    PointsRepo pointsRepo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date bookingTime;

    private Long deposit;

    private String bookingStatus;

    private int totalSeats;

    private String tableName;

    private Long point;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date payDate;

    public BookingHistoryDetailRes(tbl_Booking booking, tbl_Bill bill) {
        tbl_Deposit deposit=depositRepo.findByDepositId(booking.getDepositId());
        tbl_Points points=pointsRepo.findByPointId(bill.getPointId());

        this.bookingTime=booking.getBookingTime();
        this.deposit=deposit.getDeposit();
        this.bookingStatus=booking.getBookingStatus()==1?"Đã thanh toán!":"Chưa thanh toán!";
        this.totalSeats=booking.getTotalSeats();
        this.tableName=booking.getTableName();
        this.point=points.getPrice();
        this.payDate=bill.getPayDate();
    }
}
