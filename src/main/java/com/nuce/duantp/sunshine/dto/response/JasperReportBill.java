package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JasperReportBill implements Serializable {
    private String bookingId;
    private String customerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date bookingTime;
    List<BillReport> billReports;
    private Long deposit;
    private float sale;
    private float sumMoney;
    private float totalMoney;
}
