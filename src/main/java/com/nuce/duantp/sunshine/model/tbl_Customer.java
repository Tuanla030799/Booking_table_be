package com.nuce.duantp.sunshine.model;

import com.nuce.duantp.sunshine.dto.request.UpdateUserReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_customer")

public class tbl_Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "totalMoney")
    private Long totalMoney;

    public tbl_Customer(String email, String phoneNumber, String fullName, String password, Long totalMoney) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
        this.totalMoney = totalMoney;
    }

    public tbl_Customer(String email, String phoneNumber, String fullName, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
    }

    public tbl_Customer(tbl_Customer customer) {
        this.email = customer.email;
        this.phoneNumber = customer.phoneNumber;
        this.fullName = customer.fullName;
        this.password = customer.password;
        this.totalMoney = customer.totalMoney;
    }

    public tbl_Customer(UpdateUserReq req) {
        this.setPhoneNumber(req.getPhoneNumber());
        this.setFullName(req.getFullName());
    }
}
