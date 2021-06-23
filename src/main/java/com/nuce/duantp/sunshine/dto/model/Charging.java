package com.nuce.duantp.sunshine.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.request.ChargingReq;
import com.nuce.duantp.sunshine.dto.request.CustomerChargingReq;
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
@Table(name = "Charging")
@EntityListeners(AuditingEntityListener.class)
public class Charging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "code")
    private String code;

    @Column(name = "money")
    private float money;

    @Column(name = "status")
    private int status;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss aa")
    private Date created;

    public Charging(CustomerChargingReq chargingReq,String email) {
        this.email=email;
        this.code=chargingReq.getCode();
        this.phoneNumber=chargingReq.getPhoneNumber();
        this.money=Float.parseFloat(chargingReq.getCode().substring(2));
        this.status=0;
    }
}
