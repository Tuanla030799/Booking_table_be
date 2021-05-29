package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.dto.model.tbl_News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsHomeRes {
    private int stt;

    private String newsTitle;

    private String newsDetail;

    private String newsImage;

    public NewsHomeRes(tbl_News news,int stt) {
        this.newsTitle = news.getNewsTitle();
        String[] splits = news.getNewsDetail().split(" ");
        String str = "";
        if (splits.length > 30) {
            for (int i = 0; i < 30; i++) {
                str = str + splits[i] + " ";
            }
        } else str = news.getNewsDetail();
        this.newsDetail = str;
        this.newsImage = news.getNewsImage();
        this.stt=stt;
    }
}
