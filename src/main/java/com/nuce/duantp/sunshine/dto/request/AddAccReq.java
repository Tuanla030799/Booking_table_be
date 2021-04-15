package com.nuce.duantp.sunshine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAccReq {
    private String email;
    private String accountNo;
}
