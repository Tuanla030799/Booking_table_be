package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.request.AddAccReq;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.PointHistoryRes;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
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
    @Autowired
    SunShineService sunShineService;
    @Autowired
    SaleRepo saleRepo;
    @Autowired
    private AccountRepo accountRepo;
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
            float money = 0L;
            if (booking.getBookingStatus() == 1) {
                money = sunShineService.moneyPay(booking.getBookingId());
            }
            BookingHistoryRes data1 = new BookingHistoryRes(booking, money);
            data.add(data1);
        }
        return data;
    }

    public BookingHistoryDetailRes viewBookingHistoryDetail(String bookingId) {
        tbl_Booking booking = bookingRepository.findByBookingId(bookingId);
        float money = 0L;
        if (booking.getBookingStatus() == 1) {
            money = sunShineService.moneyPay(booking.getBookingId());
        }
        tbl_Bill bill = billRepo.findByBookingId(booking.getBookingId());
        BookingHistoryDetailRes data = new BookingHistoryDetailRes(booking, bill, money);
        return data;
    }

    public ResponseEntity<?> addAccount(AddAccReq addAccReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        tbl_BankAccount account= accountRepo.findByAccountNo(addAccReq.getAccountNo());
        if(account==null){
            List<tbl_BankAccount> bankAccountList=accountRepo.findByEmail(addAccReq.getEmail());
            for(tbl_BankAccount data:bankAccountList){
                data.setStatus(0);
                accountRepo.save(data);
            }
            tbl_BankAccount acc=new tbl_BankAccount(addAccReq.getAccountNo(),1000000L,1,addAccReq.getEmail());
            accountRepo.save(acc);
            LOGGER.warn("Add AccountAno by " + customer.get().getEmail() + "\n" + addAccReq, CustomerService.class);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_ACCOUNT_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ACCOUNT_EXISTED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    public List<tbl_Sale> getListSaleForUser(String email){
        tbl_Customer customer=customerRepo.findCustomerByEmail(email);
        List<tbl_Sale> saleList=saleRepo.findBySaleStatusAndBeneficiary(1,customer.getBeneficiary());
        return saleList;
    }
}
