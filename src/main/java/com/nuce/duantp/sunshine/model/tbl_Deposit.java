package com.nuce.duantp.sunshine.model;

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
@Table(name = "tbl_deposit") //đặt cọc
@EntityListeners(AuditingEntityListener.class)
public class tbl_Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depositId")
    private Long depositId;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "totalPersons")
    private int totalPersons;

    @Column(name = "depositStatus")
    private int depositStatus =1;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

}
