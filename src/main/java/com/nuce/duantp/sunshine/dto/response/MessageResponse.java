package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageResponse {
    private EnumResponseStatusCode status;
    private String message;


    public MessageResponse(EnumResponseStatusCode response) {
        this.status=response;
        this.message=response.label;

    }

}
