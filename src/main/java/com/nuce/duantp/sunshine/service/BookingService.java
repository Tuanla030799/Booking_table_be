package com.nuce.duantp.sunshine.service;


import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.BillRepo;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.repository.ResponseStatusCodeRepo;
import com.nuce.duantp.sunshine.repository.TableRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ResponseStatusCodeRepo responseStatusCodeRepo;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    TableRepo tableRepo;
    @Autowired
    BillRepo billRepo;

    private Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    public ResponseEntity<?> bookingTable(BookingReq bookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);

        String tableName = null;
        List<tbl_Table> tableList = tableRepo.findBySeatGreaterThanEqualOrderBySeatAsc(bookingReq.getTotalSeats());
        for (tbl_Table table : tableList) {
            Date date1 = TimeUtils.minusDate(Timestamp.valueOf(bookingReq.getBookingTime()), -3, "HOUR");
            Date date2 = TimeUtils.minusDate(Timestamp.valueOf(bookingReq.getBookingTime()), 3, "HOUR");
            List<tbl_Booking> bookingList = bookingRepository.findByBookingStatusAndTableNameAndBookingTimeBetween(0, table.getTablename(), date1, date2);
            if (bookingList.size() < table.getStillEmpty()) {
                tableName = table.getTablename();
                break;
            }
        }
        MessageResponse messageResponse = new MessageResponse();
        tbl_ResponseStatusCode responseStatusCode = new tbl_ResponseStatusCode();
        if (tableName == null) {
            responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(String.valueOf(EnumResponseStatusCode.TABLE_OFF));
            messageResponse.setMessage(responseStatusCode.getResponseStatusMessage());
            messageResponse.setStatus(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()));
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
        String str = bookingRepository.bookingTable(customer.get().getEmail(), bookingReq.getBookingTime(), bookingReq.getAccountNo(), bookingReq.getTotalSeats(), tableName);
        responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            LOGGER.warn("Booking table success by " + customer.get().getEmail() + "\n" + bookingReq, BookingService.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> orderFood(OrderFoodReq orderFoodReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        tbl_Bill bill=billRepo.findByBookingId(orderFoodReq.getBookingId());
        String str = "ADD_FOOD_SUCCESS";
        for (FoodReq data : orderFoodReq.getFoodList()) {
            str = bookingRepository.orderFood(orderFoodReq.getBookingId(), bill.getBillId(), data.getQuantity(),
                    data.getFoodId());
        }
        LOGGER.warn("Booking table success by " + customer.get().getEmail() + "\n" + orderFoodReq, BookingService.class);
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> pay(PayReq payReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str="aa";
        /*
        * TODO:
        *  thay đổi pay, thêm khuyến mãi vào
        * */
//        String str = bookingRepository.pay(customer.get().getEmail(), payReq.getAccountNo(), payReq.getBookingId(), payReq.getDiscount());
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            LOGGER.warn("Pay bill success by " + customer.get().getEmail() + "\n" + payReq, BookingService.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> cancelBooking(CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str = bookingRepository.cancelBooking(customer.get().getEmail(), cancelBookingReq.getBookingId());
        /*
         * TODO
         *  xử lý phần nhân viên xác nhận bằng thread
         * */
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            LOGGER.warn("Cancel booking success by " + customer.get().getEmail() + "\n" + cancelBookingReq,
                    BookingService.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> cancelBookingAdmin(CancelBookingReq cancelBookingReq,String email) {
        String str = bookingRepository.cancelBookingAdmin(cancelBookingReq.getBookingId());
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            LOGGER.warn("Cancel booking success by admin " + email + "\n" + cancelBookingReq,BookingService.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
