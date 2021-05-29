package com.nuce.duantp.sunshine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillReport {
    private int stt;
    private String foodName;
    private String price;
    private String money;
    private int set;

}
