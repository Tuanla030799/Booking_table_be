package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.PointHistoryRes;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.service.CustomerService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    TokenLivingService tokenLivingService;
    @GetMapping("/point-used-history")
    public List<PointHistoryRes> viewHistoryPointUse(HttpServletRequest req){
        if(tokenLivingService.checkTokenLiving(req)){
           return customerService.viewHistoryPointUse(req);
        }
        else return null;
    }

    @GetMapping("/booking-history")
    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req){
        if(tokenLivingService.checkTokenLiving(req)){
            return customerService.viewBookingHistory(req);
        }
        else return null;
    }

    @GetMapping("/booking-history-detail")
    public List<BookingHistoryDetailRes> viewBookingHistoryDetail(HttpServletRequest req){
        if(tokenLivingService.checkTokenLiving(req)){
            return customerService.viewBookingHistoryDetail(req);
        }
        else return null;
    }
}
