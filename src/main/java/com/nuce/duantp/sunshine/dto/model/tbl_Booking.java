package com.nuce.duantp.sunshine.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_booking")
@EntityListeners(AuditingEntityListener.class)
public class tbl_Booking {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
    @Id
    @Column(name = "bookingid")
    private String bookingId;

    @Column(name = "email")
    private String email;

    @Column(name = "bookingtime") //thoi gian dat ban
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date bookingTime;

    @Column(name = "totalSeats") //tổng số chỗ ngồi
    private int totalSeats;

    @Column(name = "depositId") //đặt cọc
    private Long depositId;

    @Column(name = "bookingStatus") //dùng để xe boking đã thnh toán chưa: 0: là chưa thanh toán , 1:đthanh toán
    private int bookingStatus;

    @Column(name = "tableName")
    private String tableName;

    @Column(name = "note")
    private String note = "...";

    @Column(name = "saleId")//phần trăm khuyến mãi
    private Long saleId;

    @Column(name = "confirmBooking")
    private int confirmBooking;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

    public tbl_Booking(String bookingId, String email, Date bookingTime, int totalSeats, Long depositId,
                       int bookingStatus, String tableName,String note) {
        this.bookingId = bookingId;
        this.email = email;
        this.bookingTime = bookingTime;
        this.totalSeats = totalSeats;
        this.depositId = depositId;
        this.bookingStatus = bookingStatus;
        this.tableName = tableName;
        this.note=note;
        this.confirmBooking=0;
    }


}
