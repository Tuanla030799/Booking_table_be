package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleDetail {

    private Long saleId;

    private String saleTitle;

    private String saleDetail;

    private String saleImage;

    private String beneficiary; //người được hưởng khuyễn mãi

    private String percentDiscount;

    private String totalBill;

    public SaleDetail(tbl_Sale sale) {
        this.saleId=sale.getSaleId();
        this.saleTitle=sale.getSaleTitle();
        this.saleDetail=sale.getSaleDetail();
        this.saleImage=sale.getSaleImage();
        this.beneficiary=sale.getBeneficiary();
        this.percentDiscount=String.format("%.0f", sale.getPercentDiscount()*100)+"%";
        this.totalBill= FormatMoney.formatMoney(String.valueOf(sale.getTotalBill()));
    }
}
