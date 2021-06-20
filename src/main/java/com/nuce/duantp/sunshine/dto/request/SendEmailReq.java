package com.nuce.duantp.sunshine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailReq {
    private String to;
    private String from;
    private String subject;
    private String message;
    private String name;
    private String fileName;
}
