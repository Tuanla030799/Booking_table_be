package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    private String email;

    private String phoneNumber;

    private String fullName;

    private String totalMoney;

    private String role;

    private String image;

    public UserDetail(tbl_Customer customer) {
        this.email=customer.getEmail();
        this.phoneNumber=customer.getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
        this.fullName=customer.getFullName();
        this.totalMoney= FormatMoney.formatMoney(String.valueOf(customer.getTotalMoney()));
        this.role=customer.getRole();
        this.image=customer.getImage();
    }
}
