package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDetailResponse implements Serializable {
    private String bookingId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss aa")
    private Date bookingTime;
    List<ListFoodInBooking> listFoodInBookings;
    private String deposit;
    private String percentSale;
    private String sumMoneyFood;
    private String totalMoney;
    private String status;
    private String payDate;
    private int totalSet;
    private String tableName;
    public PayDetailResponse(BookingHistoryDetail bookingHistoryDetail) {
        this.tableName=bookingHistoryDetail.getTableName();
        this.totalSet=bookingHistoryDetail.getTotalSet();
        this.percentSale= String.format("%.0f", bookingHistoryDetail.getPercentSale()*100)+" %";
        this.bookingId= bookingHistoryDetail.getBookingId();
        this.bookingTime= TimeUtils.minusDate(bookingHistoryDetail.getBookingTime(), 7, "HOUR");
        this.listFoodInBookings = bookingHistoryDetail.getListFoodInBookings();
        this.deposit= FormatMoney.formatMoney(String.valueOf(bookingHistoryDetail.getDeposit()));
        this.sumMoneyFood= FormatMoney.formatMoney(String.valueOf(bookingHistoryDetail.getSumMoneyFood()));
        String totalMoneyPay="";
        if(bookingHistoryDetail.getTotalMoney()<0){
            totalMoneyPay=
                    "Hoàn tiền "+FormatMoney.formatMoney(String.valueOf(Math.abs(bookingHistoryDetail.getTotalMoney())));
        }
        else {
            totalMoneyPay=FormatMoney.formatMoney(String.valueOf(bookingHistoryDetail.getTotalMoney()));
        }
        this.totalMoney= totalMoneyPay;
        this.status= bookingHistoryDetail.getStatus();
        this.payDate= bookingHistoryDetail.getPayDate();
    }
}
