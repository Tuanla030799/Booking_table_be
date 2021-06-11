package com.nuce.duantp.sunshine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReq {
    private String fullName;
    private String phoneNumber;
    private MultipartFile file;
    private String dateOfBirth;
    private int sex;

}
