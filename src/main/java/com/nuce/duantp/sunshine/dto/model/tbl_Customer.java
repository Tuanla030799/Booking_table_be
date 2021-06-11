package com.nuce.duantp.sunshine.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.request.UpdateUserReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "totalMoney")
    private Long totalMoney;

    @Column(name = "role")
    private String role;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "image")
    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "accStatus")
    private int accStatus;

    @Column(name = "sex")
    private int sex;

    public tbl_Customer(SignupRequest signupRequest, String password) {
        this.email = signupRequest.getEmail();
        this.phoneNumber = signupRequest.getPhoneNumber();
        this.fullName = signupRequest.getFullName();
        this.password = password;
        this.totalMoney = 0L;
        this.role = "USERS";
        this.beneficiary = "CUSTOMER";
        this.image = String.valueOf(new Date().getTime());
        this.accStatus = 1;
        this.image = "https://www.dropbox.com/s/6gkxzppp1g3we6z/1622285305745.jpg?raw=1";
        this.sex = signupRequest.getSex();
        try {
            this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(signupRequest.getDateOfBirth());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public tbl_Customer(String email, String phoneNumber, String fullName, String password, Long totalMoney) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
        this.totalMoney = totalMoney;
        this.role = "USERS";
        this.beneficiary = "CUSTOMER";
        this.accStatus = 1;

    }

    public tbl_Customer(String email, String phoneNumber, String fullName, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.password = password;
        this.role = "USERS";
        this.beneficiary = "CUSTOMER";
        this.accStatus = 1;

    }

    public tbl_Customer(tbl_Customer customer) {
        this.email = customer.email;
        this.phoneNumber = customer.phoneNumber;
        this.fullName = customer.fullName;
        this.password = customer.password;
        this.totalMoney = customer.totalMoney;
        this.role = "USERS";
        this.beneficiary = "CUSTOMER";
        this.accStatus = 1;

    }

    public tbl_Customer updateCustomer(UpdateUserReq req) {
        if (req.getFullName() != null && !req.getFullName().equals("")) this.setFullName(req.getFullName());
        if (req.getPhoneNumber() != null && !req.getPhoneNumber().equals("")) this.setPhoneNumber(req.getPhoneNumber());
        if (req.getFile() != null) this.image = String.valueOf(new Date().getTime());
        if (!req.getDateOfBirth().equals("") && req.getDateOfBirth() != null) {
            try {
                this.dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(req.getDateOfBirth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (req.getSex() == 1 || req.getSex() == 0) this.sex = req.getSex();
        return this;
    }
}
