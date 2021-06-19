package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageResponse {
    private EnumResponseStatusCode statusCode;
    private String message;


    public MessageResponse(EnumResponseStatusCode response) {
        this.statusCode =response;
        this.message=response.label;
    }

}
