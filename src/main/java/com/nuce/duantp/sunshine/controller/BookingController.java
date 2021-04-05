package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.BookingReq;
import com.nuce.duantp.sunshine.dto.request.CancelBookingReq;
import com.nuce.duantp.sunshine.dto.request.OrderFoodReq;
import com.nuce.duantp.sunshine.dto.request.PayReq;
import com.nuce.duantp.sunshine.service.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/customer")
public class BookingController {
    @Autowired
    BookingServiceImpl bookingService;


    @PostMapping("/booking")
    public ResponseEntity<?> booking(@RequestBody BookingReq bookingReq, HttpServletRequest req) {

        return bookingService.bookingTable(bookingReq,req);
    }

    @PostMapping("/order-food")
    public ResponseEntity<?> orderFood(@RequestBody OrderFoodReq orderFoodReq, HttpServletRequest req) {
        return bookingService.orderFood(orderFoodReq,req);
    }

    @PostMapping("/pay-bill")
    public ResponseEntity<?> pay(@RequestBody PayReq payReq, HttpServletRequest req) {
        return bookingService.pay(payReq,req);
    }

    @PostMapping("/cancel-booking")
    public ResponseEntity<?> cancelBooking(@RequestBody CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        return bookingService.cancelBooking(cancelBookingReq,req);
    }

    @PostMapping("/cancel-booking-admin")
    public ResponseEntity<?> cancelBookingAdmin(@RequestBody CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        return bookingService.cancelBookingAdmin(cancelBookingReq);
    }


}



