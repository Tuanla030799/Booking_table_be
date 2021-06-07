package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.config.format.FormatChar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillPay implements Serializable {
    private String bookingId;
    private String customerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date bookingTime;
    List<BillReport> billReports;
    private Long deposit;
    private Long saleId;
    private float sumMoneyFood;
    private float totalMoney;
    private String status;
    public BillPay convertChar() {
        this.customerName=FormatChar.covertToString(this.getCustomerName());
        for(BillReport data:this.billReports){
            data.setFoodName(FormatChar.covertToString(data.getFoodName()));
        }
        return  this;
    }
}
