package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.model.tbl_Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleHomeRes {
    private String saleTitle;

    private String saleDetail;

    private String saleImage;

    private float percentDiscount;//phần trăm khuyến mãi

    public SaleHomeRes(tbl_Sale sale) {
        this.saleTitle = sale.getSaleTitle();
        String[] splits = sale.getSaleDetail().split(" ");
        String str = "";
        if (splits.length > 30) {
            for (int i = 0; i < 30; i++) {
                str = str + splits[i] + " ";
            }
        } else str = sale.getSaleDetail();
        this.saleDetail = str;
        this.saleImage = sale.getSaleImage();
        this.percentDiscount = sale.getPercentDiscount();
    }
}
