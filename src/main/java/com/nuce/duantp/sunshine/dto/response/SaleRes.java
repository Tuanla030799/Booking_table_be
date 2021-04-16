package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.model.tbl_Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SaleRes {
    private String saleTitle;

    private String saleDetail;

    private String saleImage;

    private float percentDiscount;//phần trăm khuyến mãi

    public SaleRes(tbl_Sale sale) {
        this.saleTitle = sale.getSaleTitle();
        String[] splits = sale.getSaleDetail().split(" ");
        String str = "";
        for (int i = 0; i < 30; i++) {
            str = str + splits[i] + " ";
        }
        this.saleDetail=str;
        this.saleImage=sale.getSaleImage();
        this.percentDiscount=sale.getPercentDiscount();
    }
}
