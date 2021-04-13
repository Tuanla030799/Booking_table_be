package com.nuce.duantp.sunshine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPointRes {
    private Date timeUse;
    private Long pointUser;
    private Long cumulativePoints;
}
