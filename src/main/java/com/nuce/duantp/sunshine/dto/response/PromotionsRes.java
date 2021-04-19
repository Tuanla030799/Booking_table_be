package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.model.tbl_News;
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


    public PromotionsRes(tbl_News news) {
        this.promotionsTitle=news.getNewsTitle();
        String[] splits = news.getNewsDetail().split(" ");
        String str = "";
        for (int i = 0; i < 30; i++) {
            str = str + splits[i] + " ";
        }
        this.promotionsDetail=str;
        this.promotionsImage=news.getNewsImage();
    }
}
