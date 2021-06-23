package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
import com.nuce.duantp.sunshine.dto.model.Charging;
import com.nuce.duantp.sunshine.dto.request.CustomerChargingReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerChargingResponse {
    private Long id;

    private String phoneNumber;

    private String code;

    private String money;

    private String status;

    private String time;

    public CustomerChargingResponse(Charging charging) {
        this.id=charging.getId();
        this.code=charging.getCode();
        this.phoneNumber=charging.getPhoneNumber();
        this.money= FormatMoney.formatMoney(String.valueOf(charging.getMoney()));
        this.status= Validate.convertStatusCharging(charging.getStatus());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        this.time = dateFormat.format(charging.getCreated());
    }
}
