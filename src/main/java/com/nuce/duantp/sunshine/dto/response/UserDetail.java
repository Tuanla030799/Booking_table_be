package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String status;
    public UserDetail(tbl_Customer customer) {
        this.email=customer.getEmail();
        this.phoneNumber=customer.getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
        this.fullName=customer.getFullName();
        this.totalMoney= FormatMoney.formatMoney(String.valueOf(customer.getTotalMoney()));
        this.role=customer.getRole();
        this.image=customer.getImage();
        this.status=customer.getAccStatus()==1?"Đang hoạt động":"Đã bị khóa";
    }
}
