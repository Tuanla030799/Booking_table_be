package com.nuce.duantp.sunshine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.request.SaleReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_Sale")
@EntityListeners(AuditingEntityListener.class)

public class tbl_Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saleId")
    private Long saleId;

    @Column(name = "saleTitel")
    private String saleTitle;

    @Column(name = "saleDetail")
    private String saleDetail;

    @Column(name = "saleImage")
    private String saleImage;

    @Column(name = "saleStatus")
    private int saleStatus;

    @Column(name = "beneficiary")
    private String beneficiary; //người được hưởng khuyễn mãi

    @Column(name = "percentDiscount")//phần trăm khuyến mãi
    private float percentDiscount;


    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

    public tbl_Sale(SaleReq saleReq) {
        this.saleTitle=saleReq.getSaleTitle();
        this.saleDetail=saleReq.getSaleDetail();
        this.saleImage=saleReq.getSaleImage();
        this.saleStatus=1;
        this.beneficiary=saleReq.getBeneficiary();
        this.percentDiscount= saleReq.getPercentDiscount();
    }
}
