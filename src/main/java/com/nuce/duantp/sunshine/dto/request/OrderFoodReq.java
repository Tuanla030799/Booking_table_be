package com.nuce.duantp.sunshine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFoodReq {
    private String billId;
    private String bookingId;
    private int quantity;
    private List<Long> foodId;
}
