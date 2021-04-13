package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.CancelBookingReq;
import com.nuce.duantp.sunshine.dto.request.DepositReq;
import com.nuce.duantp.sunshine.dto.request.PointReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.AdminService;
import com.nuce.duantp.sunshine.service.BookingServiceImpl;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    BookingServiceImpl bookingService;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @GetMapping("/export-file")
    public ResponseEntity<?> exportFile(@RequestBody String fileName, HttpServletRequest req){
            try {
                adminService.exportReport(fileName);
                MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.SUCCESS,
                        EnumResponseStatusCode.SUCCESS.label);
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.BAD_REQUEST,
                        EnumResponseStatusCode.BAD_REQUEST.label);
                return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
            }
    }

    @PostMapping("/cancel-booking-admin")
    public ResponseEntity<?> cancelBookingAdmin(@RequestBody CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        return bookingService.cancelBookingAdmin(cancelBookingReq);
    }

    @PostMapping("/add-deposit")
    public ResponseEntity<?> addDeposit(@RequestBody @Validated DepositReq depositReq, HttpServletRequest req){
        return adminService.changeDeposit(depositReq);
    }

    @PostMapping("/add-point")
    public ResponseEntity<?> addPoint(@RequestBody @Validated PointReq pointReq, HttpServletRequest req){
        return adminService.changePoint(pointReq);
    }

}
