package com.nuce.duantp.sunshine.JasperReports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {
    private String from;
    private String to;
    private String subject;
    private String name;
    private String url;
//    private String fileName;
}
