package com.nuce.duantp.sunshine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bill")
@EntityListeners(AuditingEntityListener.class)
public class tbl_Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "billId",unique = true)
    private String billId;

    @Column(name = "pointId")
    private Long pointId;

    @Column(name = "bookingId")
    private String bookingId;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "billStatus")
    private int billStatus;

    @Column(name = "payDate")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date payDate;

    public tbl_Bill(tbl_Bill bill,Long pointId) {
        this.billId=bill.getBillId();
        this.pointId=pointId;
        this.bookingId=bill.getBookingId();
        this.discount=bill.getDiscount();
    }

    public tbl_Bill(String billId, Long pointId, String bookingId, Long discount, int billStatus) {
        this.billId = billId;
        this.pointId = pointId;
        this.bookingId = bookingId;
        this.discount = discount;
        this.billStatus = billStatus;
    }
}
