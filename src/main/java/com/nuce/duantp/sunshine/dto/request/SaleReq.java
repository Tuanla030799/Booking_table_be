package com.nuce.duantp.sunshine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleReq {
    private String saleTitle;

    private String saleDetail;

    private String saleImage;

    private String beneficiary; //người được hưởng khuyễn mãi

    private float percentDiscount;
}
