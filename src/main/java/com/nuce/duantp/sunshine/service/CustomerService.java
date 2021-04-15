package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.request.AddAccReq;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.PointHistoryRes;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Bill;
import com.nuce.duantp.sunshine.model.tbl_Booking;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_ResponseStatusCode;
import com.nuce.duantp.sunshine.repository.BillRepo;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.ResponseStatusCodeRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BillRepo billRepo;
    @Autowired
    private ResponseStatusCodeRepo responseStatusCodeRepo;
    private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    
    public List<PointHistoryRes> viewHistoryPointUse(HttpServletRequest req) {
        ModelMapper mapper = new ModelMapper();
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.findAllByEmailAndBookingStatus(customer.get().getEmail(), 1);
        List<PointHistoryRes> pointHistoryResList = new ArrayList<>();
        for (tbl_Booking data : bookingList) {
            tbl_Bill bill = billRepo.findAllByBookingIdAndDiscountGreaterThan(data.getBookingId(), 0L);
            PointHistoryRes pointHistoryRes = mapper.map(bill, PointHistoryRes.class);
            pointHistoryResList.add(pointHistoryRes);
        }
        return pointHistoryResList;
    }

    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.findByEmail(customer.get().getEmail());
        List<BookingHistoryRes> data = new ArrayList<>();
        for (tbl_Booking booking : bookingList) {
            /*
            * TODO fix
            * */
//            BookingHistoryRes data1 = new BookingHistoryRes(booking);
//            data.add(data1);
        }
        return data;
    }

    public BookingHistoryDetailRes viewBookingHistoryDetail(String bookingId) {
        tbl_Booking booking = bookingRepository.findByBookingId(bookingId);
        tbl_Bill bill = billRepo.findByBookingId(booking.getBookingId());
        BookingHistoryDetailRes data = new BookingHistoryDetailRes(booking, bill);
        return data;
    }

    public ResponseEntity<?> addAccount(AddAccReq addAccReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String str = customerRepo.addAccount(addAccReq.getEmail(),addAccReq.getAccountNo());
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        if (str.contains("SUCCESS")) {
            LOGGER.warn("Add AccountAno by " + customer.get().getEmail() + "\n" + addAccReq, CustomerService.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
