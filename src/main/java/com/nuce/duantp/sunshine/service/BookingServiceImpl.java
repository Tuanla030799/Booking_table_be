package com.nuce.duantp.sunshine.service;


import com.nuce.duantp.sunshine.dto.request.BookingReq;
import com.nuce.duantp.sunshine.dto.request.CancelBookingReq;
import com.nuce.duantp.sunshine.dto.request.OrderFoodReq;
import com.nuce.duantp.sunshine.dto.request.PayReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_ResponseStatusCode;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.repository.ResponseStatusCodeRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class BookingServiceImpl {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ResponseStatusCodeRepo responseStatusCodeRepo;
    @Autowired
    AuthTokenFilter authTokenFilter;

     
    public ResponseEntity<?> bookingTable(BookingReq bookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str = bookingRepository.bookingTable(customer.get().getEmail(), bookingReq.getBookingTime(),
                bookingReq.getAccountNo(),
                bookingReq.getTotalSeats());
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

     
    public ResponseEntity<?> orderFood(OrderFoodReq orderFoodReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str = "ADD_FOOD_SUCCESS";
        for (Long data : orderFoodReq.getFoodId()) {
            str = bookingRepository.orderFood(customer.get().getEmail(), orderFoodReq.getBookingId(),
                    orderFoodReq.getBillId(), orderFoodReq.getQuantity(), data);
        }
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

     
    public ResponseEntity<?> pay(PayReq payReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str = bookingRepository.pay(customer.get().getEmail(),payReq.getAccountNo(),payReq.getBookingId(),
                payReq.getDiscount());
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

     
    public ResponseEntity<?> cancelBooking(CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str = bookingRepository.cancelBooking(customer.get().getEmail(),cancelBookingReq.getBookingId());
        /*
        * TODO
        *  xử lý phần nhân viên xác nhận bằng thread
        * */
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

     
    public ResponseEntity<?> cancelBookingAdmin(CancelBookingReq cancelBookingReq) {
        String str = bookingRepository.cancelBookingAdmin(cancelBookingReq.getBookingId());
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
