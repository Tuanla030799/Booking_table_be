package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private int sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date dateOfBirth;
    public UserDetail(tbl_Customer customer) {
        this.email=customer.getEmail();
        this.phoneNumber=customer.getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
        this.fullName=customer.getFullName();
        this.totalMoney= FormatMoney.formatMoney(String.valueOf(customer.getTotalMoney()));
        this.role=customer.getRole();
        this.image=customer.getImage();
        this.status=customer.getAccStatus()==1?"Đang hoạt động":"Đã bị khóa";
        this.sex=customer.getSex();

        this.dateOfBirth= TimeUtils.minusDate(customer.getDateOfBirth(), 7, "HOUR");
    }
}
