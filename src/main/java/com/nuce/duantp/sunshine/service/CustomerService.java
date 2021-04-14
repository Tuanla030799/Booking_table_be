package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.PointHistoryRes;
import com.nuce.duantp.sunshine.model.tbl_Bill;
import com.nuce.duantp.sunshine.model.tbl_Booking;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.BillRepo;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<PointHistoryRes> viewHistoryPointUse(HttpServletRequest req){
        ModelMapper mapper=new ModelMapper();
        Optional<tbl_Customer> customer=authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList=bookingRepository.findAllByEmailAndBookingStatus(customer.get().getEmail(),1);
        List<PointHistoryRes> pointHistoryResList =new ArrayList<>();
        for(tbl_Booking data:bookingList){
            tbl_Bill bill=billRepo.findAllByBookingIdAndDiscountGreaterThan(data.getBookingId(),0L);
            PointHistoryRes pointHistoryRes =mapper.map(bill, PointHistoryRes.class);
            pointHistoryResList.add(pointHistoryRes);
        }
        return pointHistoryResList;
    }

    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req){
        Optional<tbl_Customer> customer=authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList=bookingRepository.findByEmail(customer.get().getEmail());
        List<BookingHistoryRes> data=new ArrayList<>();
        for (tbl_Booking booking:bookingList){
            BookingHistoryRes data1=new BookingHistoryRes(booking);
            data.add(data1);
        }
        return data;
    }

    public List<BookingHistoryDetailRes> viewBookingHistoryDetail(HttpServletRequest req){
        Optional<tbl_Customer> customer=authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList=bookingRepository.findByEmail(customer.get().getEmail());
        List<BookingHistoryDetailRes> data=new ArrayList<>();
        for (tbl_Booking booking:bookingList){
            tbl_Bill bill=billRepo.findByBookingId(booking.getBookingId());
            BookingHistoryDetailRes data1=new BookingHistoryDetailRes(booking,bill);
            data.add(data1);
        }
        return data;
    }
}
