package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.BookingReq;
import com.nuce.duantp.sunshine.dto.request.CancelBookingReq;
import com.nuce.duantp.sunshine.dto.request.OrderFoodReq;
import com.nuce.duantp.sunshine.dto.request.PayReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.BookingServiceImpl;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customer")
public class BookingController {
    @Autowired
    BookingServiceImpl bookingService;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    TokenLivingService tokenLivingService;
    @PostMapping("/booking")
    public ResponseEntity<?> booking(@RequestBody BookingReq bookingReq, HttpServletRequest req) {
        if(tokenLivingService.checkTokenLiving(req)){
            return bookingService.bookingTable(bookingReq,req);
        }
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/order-food")
    public ResponseEntity<?> orderFood(@RequestBody OrderFoodReq orderFoodReq, HttpServletRequest req) {
        if(tokenLivingService.checkTokenLiving(req)){
            return bookingService.orderFood(orderFoodReq,req);
        }
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/pay-bill")
    public ResponseEntity<?> pay(@RequestBody PayReq payReq, HttpServletRequest req) {
        if(tokenLivingService.checkTokenLiving(req)){
            return bookingService.pay(payReq,req);
        }
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/cancel-booking")
    public ResponseEntity<?> cancelBooking(@RequestBody CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        if(tokenLivingService.checkTokenLiving(req)){
            return bookingService.cancelBooking(cancelBookingReq,req);
        }
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }




}



