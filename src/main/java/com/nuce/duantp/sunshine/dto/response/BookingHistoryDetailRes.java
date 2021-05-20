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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss aa")
    private Date bookingTime;

    private Long deposit; //tien coc

    private String bookingStatus;

    private int totalSeats;

    private String tableName;

    private Long point; //tien thuong ung voi tong hoa don

    private float moneyPay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date payDate;

//    private float disCount;

    private String bookingId;
//    public BookingHistoryDetailRes(tbl_Booking booking, tbl_Bill bill,float money) {

//
//        this.bookingTime=booking.getBookingTime();
//        this.deposit=deposit.getDeposit();
//        this.bookingStatus=booking.getBookingStatus()==1?"Đã thanh toán!":"Chưa thanh toán!";
//        this.totalSeats=booking.getTotalSeats();
//        this.tableName=booking.getTableName();
//        this.point=points.getPrice();
//        this.payDate=bill.getPayDate();
//        this.moneyPay=money;
//    }
}
