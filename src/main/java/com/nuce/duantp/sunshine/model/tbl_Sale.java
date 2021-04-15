package com.nuce.duantp.sunshine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "tbl_Promotions")
@EntityListeners(AuditingEntityListener.class)

public class tbl_Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionsId")
    private Long promotionsId;

    @Column(name = "promotionsTitel")
    private String promotionsTitel;

    @Column(name = "promotionsDitail")
    private String promotionsDitail;

    @Column(name = "promotionsImage", length = 1000)
    private String promotionsImage;

    @Column(name = "promotionsStatus")
    private int promotionsStatus;

    @Column(name = "beneficiary")
    private String beneficiary; //người được hưởng khuyễn mãi

    @Column(name = "percentDiscount")//phần trăm khuyến mãi
    private float percentDiscount;


    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;



}
