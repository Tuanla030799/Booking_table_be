//package com.nuce.duantp.sunshine.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "tbl_bankaccount")
//public class tbl_BankAccount {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "accountNo")
//    private String accountNo;
//
//    @Column(name = "balance")
//    private Long balance;
//
//    @Column(name = "status")
//    private int status;
//
//    @Column(name = "email")
//    private String email;
//
//    public tbl_BankAccount(String accountNo, Long balance, int status, String email) {
//        this.accountNo = accountNo;
//        this.balance = balance;
//        this.status = status;
//        this.email = email;
//    }
//}
