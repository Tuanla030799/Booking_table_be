package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.model.tbl_Promotions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PromotionsRes {
    private String promotionsTitle;

    private String promotionsDetail;

    private String promotionsImage;


    public PromotionsRes(tbl_Promotions promotions) {
        this.promotionsTitle=promotions.getPromotionsTitle();
        String[] splits = promotions.getPromotionsDetail().split(" ");
        String str = "";
        for (int i = 0; i < 30; i++) {
            str = str + splits[i] + " ";
        }
        this.promotionsDetail=str;
        this.promotionsImage=promotions.getPromotionsImage();
    }
}
