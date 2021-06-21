package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponse {
    private Long saleId;

    private String saleTitle;

    private String saleDetail;

    private String saleImage;

    private String saleStatus;

    private String beneficiary;

    private String percentDiscount;

    private String totalBill;

    public SaleResponse(tbl_Sale sale) {
        this.saleId=sale.getSaleId();
        this.saleTitle=sale.getSaleTitle();
        this.saleImage=sale.getSaleImage();
        this.saleStatus=sale.getSaleStatus()==1?"Còn khuyến mãi":"Hết khuyến mãi";
        this.beneficiary=sale.getBeneficiary();
        this.percentDiscount=String.format("%.0f", sale.getPercentDiscount() * 100) + "%";
        this.totalBill= FormatMoney.formatMoney(String.valueOf(sale.getTotalBill()));
    }
}
