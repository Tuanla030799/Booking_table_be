package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.dto.model.tbl_News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class NewsRes {
    private String newsTitle;

    private String newsDetail;

    private String newsImage;


    public NewsRes(tbl_News news,String image) {
        this.newsTitle =news.getNewsTitle();
        String[] splits = news.getNewsDetail().split(" ");
        String str = "";
        for (int i = 0; i < 30; i++) {
            str = str + splits[i] + " ";
        }
        this.newsDetail =str;
        this.newsImage =image;
    }
}
