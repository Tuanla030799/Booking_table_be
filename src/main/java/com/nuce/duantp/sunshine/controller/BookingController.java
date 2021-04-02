package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.BookingReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_ResponseStatusCode;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.repository.ResponseStatusCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;
//    private BookingRepoImpl bookingRepository;
    @Autowired
   private ResponseStatusCodeRepo responseStatusCodeRepo;

    public BookingController() {
    }

    @PostMapping("booking/")
    public ResponseEntity<?> booking(@RequestBody BookingReq bookingReq){

       String str= bookingRepository.bookingTable(bookingReq.getEmail(),bookingReq.getBookingTime(),
                bookingReq.getAccountNo(),
                bookingReq.getTotalSeats());
        tbl_ResponseStatusCode responseStatusCode= responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response=new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS"))
        {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
