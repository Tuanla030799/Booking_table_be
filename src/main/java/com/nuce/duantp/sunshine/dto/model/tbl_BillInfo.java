package com.nuce.duantp.sunshine.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_BillInfo")
public class tbl_BillInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "billId")
    private String billId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "foodid")
    private Long foodId;

    public tbl_BillInfo(String billId, int quantity, Long foodId) {
        this.billId = billId;
        this.quantity = quantity;
        this.foodId = foodId;
    }
}
