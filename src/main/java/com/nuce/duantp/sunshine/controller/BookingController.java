package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.BookingReq;
import com.nuce.duantp.sunshine.dto.request.CancelBookingReq;
import com.nuce.duantp.sunshine.dto.request.OrderFoodReq;
import com.nuce.duantp.sunshine.dto.request.PayReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.BookingService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final AuthTokenFilter authTokenFilter;
    private final TokenLivingService tokenLivingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/booking")
    public ResponseEntity<?> booking(@RequestBody BookingReq bookingReq, HttpServletRequest req) {
        if(tokenLivingService.checkTokenLiving(req)){
            return bookingService.bookingTable(bookingReq,req);
        }
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/get-deposit")
    public ResponseEntity<?> getDeposit(@RequestBody BookingReq bookingReq, HttpServletRequest req) {
        if(tokenLivingService.checkTokenLiving(req)){
            return bookingService.getDeposit(bookingReq,req);
        }
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/order-food")
    public ResponseEntity<?> orderFood(@RequestBody OrderFoodReq orderFoodReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        tbl_Booking booking=bookingRepository.findByBookingId(orderFoodReq.getBookingId());

        if(tokenLivingService.checkTokenLiving(req)&&customer.get().getEmail().equals(booking.getEmail())){
            return bookingService.orderFood(orderFoodReq,customer.get().getEmail());
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



