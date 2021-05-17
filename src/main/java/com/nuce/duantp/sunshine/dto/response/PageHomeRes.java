package com.nuce.duantp.sunshine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageHomeRes {
    List<FoodHomeRes> foodHomeRes;
    List<SaleHomeRes> saleHomeRes;
    List<NewsHomeRes> newsHomeRes;
}
