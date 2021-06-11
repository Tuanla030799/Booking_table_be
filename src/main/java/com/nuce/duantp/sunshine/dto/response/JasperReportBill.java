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
public class JasperReportBill implements Serializable {
    private String bookingId;
    private String customerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss aa")
    private Date bookingTime;
    List<ListFoodInBooking> listFoodInBookings;
    private Long deposit;
    private float sale;
    private float sumMoney;
    private float totalMoney;

    public JasperReportBill convertChar() {
        this.customerName=FormatChar.covertToString(this.getCustomerName());
        for(ListFoodInBooking data:this.listFoodInBookings){
            data.setFoodName(FormatChar.covertToString(data.getFoodName()));
        }
        return  this;
    }
}
