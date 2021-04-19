package com.nuce.duantp.sunshine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsReq {
    private String newsTitle;

    private String newsDetail;

    private String newsImage;
}
