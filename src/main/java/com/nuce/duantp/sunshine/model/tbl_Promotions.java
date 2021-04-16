package com.nuce.duantp.sunshine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.request.NewsReq;
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

public class tbl_Promotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionsId")
    private Long promotionsId;

    @Column(name = "promotionsTitel")
    private String promotionsTitle;

    @Column(name = "promotionsDitail")
    private String promotionsDetail;

    @Column(name = "promotionsImage")
    private String promotionsImage;

    @Column(name = "promotionsStatus")
    private int promotionsStatus;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

    public tbl_Promotions(NewsReq newsReq) {
        this.promotionsDetail=newsReq.getPromotionsDetail();
        this.promotionsTitle=newsReq.getPromotionsTitle();
        this.promotionsImage=newsReq.getPromotionsImage();
        this.promotionsStatus=1;
    }
}
