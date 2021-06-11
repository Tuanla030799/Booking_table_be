package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingHistoryDetail implements Serializable {
    private String bookingId;//
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss aa")
    private Date bookingTime;//
    List<ListFoodInBooking> listFoodInBookings;//
    private Long deposit;//
    private Long saleId;//
    private float sumMoneyFood;//
    private float totalMoney;//
    private String status;//
    private String payDate;//
    private String tableName;
    private int totalSet;
    private float percentSale;
}
