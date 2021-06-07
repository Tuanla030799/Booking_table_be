package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.config.format.FormatChar;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDetailResponse implements Serializable {
    private String bookingId;
    private String customerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date bookingTime;
    List<BillReport> billReports;
    private String deposit;
    private String saleTitle;
    private String sumMoneyFood;
    private String totalMoney;
    private String status;
    public PayDetailResponse convertChar() {
        this.customerName=FormatChar.covertToString(this.getCustomerName());
        for(BillReport data:this.billReports){
            data.setFoodName(FormatChar.covertToString(data.getFoodName()));
        }
        return  this;
    }

    public PayDetailResponse(BillPay billPay,String saleTitle) {
        this.saleTitle=saleTitle;
        this.bookingId= billPay.getBookingId();
        this.customerName= billPay.getCustomerName();
        this.bookingTime=billPay.getBookingTime();
        this.billReports=billPay.getBillReports();
        this.deposit= FormatMoney.formatMoney(String.valueOf(billPay.getDeposit()));
        this.sumMoneyFood= FormatMoney.formatMoney(String.valueOf(billPay.getSumMoneyFood()));
        this.totalMoney= FormatMoney.formatMoney(String.valueOf(billPay.getTotalMoney()));
        this.status=billPay.getStatus();
    }
}
