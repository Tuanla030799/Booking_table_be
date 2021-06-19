package com.nuce.duantp.sunshine.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class News {
    private MultipartFile file;
    private String newsTitle;
    private String newsDetail;
}
