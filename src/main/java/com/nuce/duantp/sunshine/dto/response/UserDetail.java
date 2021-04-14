package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.model.tbl_Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    private String email;

    private String phoneNumber;

    private String fullName;

    private Long totalMoney;

    private String role;

    public UserDetail(Optional<tbl_Customer> customer) {
        this.email=customer.get().getEmail();
        this.phoneNumber=customer.get().getPhoneNumber();
        this.fullName=customer.get().getFullName();
        this.totalMoney=customer.get().getTotalMoney();
        this.role=customer.get().getRole();
    }
}
